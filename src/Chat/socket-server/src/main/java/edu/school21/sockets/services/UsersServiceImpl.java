package edu.school21.sockets.services;

import edu.school21.sockets.app.Main;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.ChatroomsRepository;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public synchronized void signUp(String name, String password) {
        PasswordEncoder passwordEncoder = (BCryptPasswordEncoder)Main.context.getBean("passwordEncoder");
        usersRepository.save(new User(1L, name, passwordEncoder.encode(password)));
    }

    @Override
    public synchronized boolean singIn(String name, String password) {
        Optional<User> user = usersRepository.findByName(name);
        PasswordEncoder passwordEncoder = (BCryptPasswordEncoder)Main.context.getBean("passwordEncoder");
        return user.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent();
    }

    public synchronized User findByName(String name) {
        return usersRepository.findByName(name).orElse(null);
    }
}
