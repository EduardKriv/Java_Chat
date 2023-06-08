package edu.school21.sockets.app;

import com.beust.jcommander.JCommander;
import edu.school21.sockets.config.ClientConfig;
import edu.school21.sockets.models.Client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {
    private final static String IP = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientConfig.class);
        context.getBean(JCommander.class).parse(args);

        new Client(IP, Args.getPort());
    }
}