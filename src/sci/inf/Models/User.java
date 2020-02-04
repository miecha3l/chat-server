package sci.inf.Models;

import sci.inf.Server.Server;
import sci.inf.Server.UserThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class User {

    public User(String un, String ip, Socket socket) throws IOException {
        this.ip = ip;
        this.username = un;
        this.userSocket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.online = true;
    }

    public String username;
    public String ip;
    public Socket userSocket;
    public Thread uThread;
    public DataInputStream in;
    public DataOutputStream out;
    boolean online;


    public void logIn(){
        uThread = new Thread(new UserThread(this));
        uThread.start();
    }

    public void logOut(){
        this.online = false;
    }

    public boolean isOnline() {return online;}

    @Override
    public String toString() {
        return username;
    }

    public void sendNotification(String type, String receiver, String message) {
        Server.getInstance().outgoingMessages.push(Message.updateMessage(message, receiver, type));
    }

    public void sendUserList(String receiver) {
        Server.getInstance().outgoingMessages.push(Message.updateMessage(Server.getInstance().getUserList(), receiver, "USER_LIST"));
    }

    public void sendMessage(Message message) throws IOException {
        out.writeUTF(message.toString());
    }
}
