package edu.school21.sockets.models;

import edu.school21.network.Chats;

import java.util.LinkedList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String password;

    private final List<Chats> createdRooms;
    private final List<Chats> chatRooms;

    public User() {
        createdRooms = new LinkedList<>();
        chatRooms = new LinkedList<>();
    }

    public User(Long id, String name, String password) {
        this();
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void addCreatedRoom(Chatroom room) {
        createdRooms.add(room);
    }

    public void addChatRoom(Chatroom room) {
        chatRooms.add(room);
    }

    public String getName() {
        return name;
    }

    public List<Chats> getChatrooms() {
        return chatRooms;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdRooms'" + createdRooms +  '\'' +
                ", chatRooms'" + chatRooms +  '\'' +
                '}';
    }
}
