package edu.school21.sockets.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Args {
    @Parameter(names = {"--server-port"})
    private static int serverPort;

    private static final Args instance = new Args();

    private Args() {}

    public static Args getInstance() { return instance; }

    public static int getPort() {
        return serverPort;
    }
}
