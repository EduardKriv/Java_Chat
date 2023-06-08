package edu.school21.sockets.models;

import edu.school21.network.Chats;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Chatroom implements Chats {
    private Long id;
    private String chatName;
    private User creator;
    private final List<Message> messagesList;

    public Chatroom() {
        this.messagesList = new LinkedList<>();
    }

    public Chatroom(Long id, String chatName, User creator) {
        this();
        this.id = id;
        this.chatName = chatName;
        this.creator = creator;
    }

    public Chatroom(String chatName) {
        this();
        this.chatName = chatName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return Objects.equals(id, ((Chatroom) obj).id);
    }

    @Override
    public String toString() {
        return String.format("{ id = %d, name = \"%s\", creator = %s, messages = %s }",
                id, chatName, creator.getName(), messagesList);
    }

    public Long getId() {
        return id;
    }

    public String getChatName() {
        return chatName;
    }

    public User getCreator() {
        return creator;
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
