package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.ChatroomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatroomsServiceImpl implements ChatroomsService {
    private final ChatroomsRepository chatroomsRepository;

    @Autowired
    public ChatroomsServiceImpl(ChatroomsRepository chatroomsRepository) {
        this.chatroomsRepository = chatroomsRepository;
    }

    @Override
    public synchronized void createRoom(Chatroom room, User creator) {
        room.setCreator(creator);
        chatroomsRepository.save(room);
        creator.addCreatedRoom(room);
    }

    @Override
    public synchronized List<Chatroom> getChatroomList() {
        return chatroomsRepository.findAll();
    }
}
