package edu.school21.network;

import java.net.Socket;
import java.util.List;

public interface CommunicatingClients {
    String getName();
    List<Chats> getChatrooms();
    Chats getActiveRoom();
}
