package edu.school21.sockets.server;

import edu.school21.sockets.app.Main;
import edu.school21.network.TCPConnectionListener;
import edu.school21.network.TCPConnection;
import edu.school21.sockets.models.ChaterUser;
import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.services.ChatroomsService;
import edu.school21.sockets.services.UsersServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Server implements TCPConnectionListener {
    private final UsersServiceImpl usersService;
    private final ChatroomsService chatroomsService;
    private final List<TCPConnection> connections = new LinkedList<>();

    public Server(int port) throws IOException {
        this.usersService = Main.context.getBean(UsersServiceImpl.class);
        this.chatroomsService = Main.context.getBean(ChatroomsService.class);


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running!");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                new Thread(() -> {
                    try {
                        Optional<ChaterUser> activeUser = authorization(clientSocket);
                        if (activeUser.isPresent()) {
                            new TCPConnection(this, clientSocket, activeUser.get());
                        } else {
                            clientSocket.close();
                        }

                    } catch (IOException e) {
                        System.out.println("Disconnect . . .");
                    }

                }).start();
            }
        }
    }

    private Optional<ChaterUser> authorization(Socket socket) throws IOException {
        final String USER_NAME = "Enter username";
        final String USER_PASSWORD = "Enter password";
        final String MAIN_MENU = "\t1. singIn\n\t2. signUp\n\t3. Exit";
        final String ROOM_MENU = "\t1. Create room\n\t2. Choice room\n\t3. Exit";
        String EXIT = "3";

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());
        ChaterUser activeUser = null;

        boolean isSuccess = true;

        try {
            String choice = clientSurvey(MAIN_MENU, in, out);
            if (choice.equals(EXIT)) return Optional.empty();

            String name = clientSurvey(USER_NAME, in, out);
            String password = clientSurvey(USER_PASSWORD, in, out);

            switch (choice) {
                case "1"  -> isSuccess = usersService.singIn(name, password);
                case "2"  -> usersService.signUp(name, password);
                default -> {
                    return Optional.empty();
                }
            }

            if (isSuccess) {

                activeUser = new ChaterUser(usersService.findByName(name));
                choice = clientSurvey(ROOM_MENU, in, out);

                switch (choice) {
                    case "1" -> {
                        String roomName = clientSurvey("Enter roomname", in, out);
                        Chatroom addedRoom = new Chatroom(roomName);
                        chatroomsService.createRoom(addedRoom, activeUser);

                        System.out.println(activeUser);
                        System.out.println(addedRoom);
                        System.out.println("Success");
                    }
                    case "2"  -> {
                        List<Chatroom> roomsList = chatroomsService.getChatroomList();
                        String selectionBox = toSelectionBox(roomsList);
                        choice = clientSurvey(selectionBox, in, out);
                        activeUser.setActiveRoom(roomsList.get(Integer.parseInt(choice) - 1));
                    }

                    default -> {
                        return Optional.empty();
                    }
                }
            } else {
                System.out.println("Oshibka blyat!");
            }


        } catch (IOException e) {
            System.out.println("Error authorization");
            socket.close();
        }

        return Optional.ofNullable(activeUser);
    }
    private String toSelectionBox(List<Chatroom> list) {
        StringBuilder resString = new StringBuilder();

        int bound = list.size();
        for (int i = 0; i < bound; i++) {
            resString.append(String.format("%d. %s\n", i + 1, list.get(i).getChatName()));
        }
        resString.append(String.format("%d. Exit", bound + 1));

        return resString.toString();
    }

    private String clientSurvey(String request, DataInputStream in, DataOutputStream out) throws IOException {
        out.writeUTF(request);
        out.flush();
        return in.readUTF();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        tcpConnection.sendMessage("Start messaging");
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String message) {
        System.out.println("Server message: " + message);
        System.out.println("Count users: " + connections.size());

        connections.stream().filter((e) -> e.getActiveUser().getChatrooms().contains(tcpConnection.getActiveUser().getActiveRoom()))
                .forEach((e) -> e.sendMessage(tcpConnection.getActiveUser().getName() + ": " + message));
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        connections.forEach((e) -> e.sendMessage(tcpConnection.getActiveUser().getName() + " have left the chat"));

        System.out.println(tcpConnection.getActiveUser().getName() + " disconnected");
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        tcpConnection.disconnect();
    }
}
