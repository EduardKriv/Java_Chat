package edu.school21.sockets.models;

import edu.school21.network.Chats;
import edu.school21.network.CommunicatingClients;

public class ChaterUser extends User implements CommunicatingClients {
    Chatroom activeRoom;

    public ChaterUser(User user) {
        super(user.getId(), user.getName(), user.getPassword());
    }

    @Override
    public Chats getActiveRoom() {
        return activeRoom;
    }

    public void setActiveRoom(Chatroom chat) {
        this.activeRoom = chat;
        super.addChatRoom(chat);
    }
}
