package edu.school21.sockets.app;

import com.beust.jcommander.JCommander;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {
    public static ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);

    public static void main(String[] args) throws IOException {
        context.getBean(JCommander.class).parse(args);

        new Server(Args.getPort());
    }
}