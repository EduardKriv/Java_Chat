package edu.school21.sockets.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import edu.school21.network.TCPConnection;
import edu.school21.network.TCPConnectionListener;

import java.io.*;
import java.net.Socket;

public class Client implements TCPConnectionListener {
    private TCPConnection connection;
    private Thread outTread;
    private BufferedReader br;

    public Client(String ip, int port) throws IOException {
//        BufferedWriter output = new BufferedWriter(new OutputStreamWriter((new Socket(ip, port)).getOutputStream()));

        br = new BufferedReader(new InputStreamReader(System.in));

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement json = gson.fromJson(br.readLine(), JsonElement.class);
//
//        String jsonInString = gson.toJson(json);
//
//        System.out.println(jsonInString);

        connection = new TCPConnection(this, new Socket(ip, port));

        outTread = new Thread(() -> {
            try {

                while (!outTread.isInterrupted()) {
                    connection.sendMessage(br.readLine());
                }

            } catch (IOException e) {
                Client.this.onException(connection, e);
            }
        });

        outTread.start();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        System.out.println("Hello from Server!");
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String message) {
            System.out.println(message);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Disconnect ...");
        outTread.interrupt();
        System.exit(0);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("Exception: " + e.getMessage());
        tcpConnection.disconnect();
    }
}
