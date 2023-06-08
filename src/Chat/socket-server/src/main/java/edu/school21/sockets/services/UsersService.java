package edu.school21.sockets.services;

public interface UsersService {
    void signUp(String name, String password);
    boolean singIn(String name, String password);
}
