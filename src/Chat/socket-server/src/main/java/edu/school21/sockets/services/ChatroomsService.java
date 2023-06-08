package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;

import java.util.List;

public interface ChatroomsService {
    void createRoom(Chatroom room, User creator);
    List<Chatroom> getChatroomList();
}
