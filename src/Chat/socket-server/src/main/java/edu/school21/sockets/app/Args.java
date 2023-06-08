package edu.school21.sockets.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.stereotype.Component;

@Component
@Parameters(separators = "=")
public class Args {
    @Parameter(names = {"--port"})
    private static int port;

    private static final Args instance = new Args();

    private Args() {}

    public static Args getInstance() { return instance; }

    public static int getPort() {
        return port;
    }
}
