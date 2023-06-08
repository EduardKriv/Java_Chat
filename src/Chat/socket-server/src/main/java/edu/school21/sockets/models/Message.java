package edu.school21.sockets.models;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private User sender;
    private String text;
    private LocalDateTime time ;

    public Message(Long id, User sender, String text, LocalDateTime time) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
