package edu.school21.sockets.config;

import com.beust.jcommander.JCommander;
import edu.school21.sockets.app.Args;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("edu.school21.sockets")
@Configuration
public class ClientConfig {
    @Bean(name = "jcommander")
    public JCommander getJcommander() {
        return JCommander.newBuilder()
                .addObject(Args.getInstance())
                .build();
    }
}
