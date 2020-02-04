package sci.inf;

import sci.inf.Server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Server.getInstance().start(8888);
    }
}
