package edu.school21.network;

import java.io.*;
import java.net.Socket;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private CommunicatingClients communicatingClient;

    private final DataOutputStream output;

    public TCPConnection(TCPConnectionListener eventListener, Socket socket, CommunicatingClients communicatingClients) throws IOException {
        this(eventListener, socket);
        this.communicatingClient = communicatingClients;
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;

        output = new DataOutputStream(socket.getOutputStream());

        eventListener.onConnectionReady(TCPConnection.this);

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try (DataInputStream input = new DataInputStream((socket.getInputStream()))) {

                    while (!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConnection.this, input.readUTF());
                    }

                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                }
            }
        });

        rxThread.start();
    }

    public synchronized void sendMessage(String message) {
        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error sendMessage!!!");
            eventListener.onException(TCPConnection.this, e);
        }
    }

    public synchronized void disconnect() {
        try {
            rxThread.interrupt();
            socket.close();
            eventListener.onDisconnect(TCPConnection.this);
        } catch (IOException e) {
            System.out.println("Error disconnect!");
            throw new RuntimeException(e);
        }
    }

//    public CommunicatingClients getSender() {
//        return communicatingClient;
//    }

    public CommunicatingClients getActiveUser() {
        return communicatingClient;
    }

//    public List<Chats> getChatrooms() { return communicatingClient.getChatrooms(); }
}
