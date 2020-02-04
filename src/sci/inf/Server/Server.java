package sci.inf.Server;

import sci.inf.Models.Message;
import sci.inf.Models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Server {

    //singleton implementation
    private static Server instance;
    private Server(){
        this.userHandler = new Thread(new UserHandler());
        this.messageHandler = new Thread(new MessageHandler());
        this.incomingMessages = new Stack<>();
        this.outgoingMessages = new Stack<>();
    }
    public static Server getInstance(){
        return instance == null ? instance = new Server() : instance;
    }

    //public fields
    public int port;
    public List<User> users =  Collections.synchronizedList(new ArrayList<User>());
    public Stack<Message> incomingMessages;
    public Stack<Message> outgoingMessages;
    public ServerSocket socket;
    public boolean isRunning;



    //server threads
    private Thread userHandler;
    private Thread messageHandler;

    //methods
    public void start(int port) throws IOException {
        instance.port = port;
        instance.socket = new ServerSocket(this.port);
        instance.userHandler.start();
        instance.messageHandler.start();
        instance.isRunning = true;
        new Thread(new ServerCli()).start();
        System.out.println("Server running on localhost:" + port);
    }

    public String getUserList() {
        String ret = "";
        for(int i = 0; i < users.size(); i++){
            ret += i == users.size() - 1 ? users.get(i) : users.get(i) + ",";
        }
        return ret;
    }

    private String getIncomingMessages(){
        String ret = "";
        for(Message msg : instance.incomingMessages) ret += msg.toString() + "\n";
        return ret;
    }

    public void sendNotificationToAll(String type, String message) throws IOException {
        for(User user : users) user.sendNotification(type, user.username, message);
    }

    private class ServerCli implements Runnable{

        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            while(Server.getInstance().isRunning){
                var msg = sc.next();

                switch (msg){
                    case "list_user": showUserList(); break;
                    case "list_incoming": showIncomingMessages(); break;
                }
            }
        }

        private void showIncomingMessages() {
            System.out.print(Server.getInstance().getIncomingMessages());
        }

        private void showUserList(){
            System.out.println(Server.getInstance().getUserList());
        }
    }
}