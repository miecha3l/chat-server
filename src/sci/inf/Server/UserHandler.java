package sci.inf.Server;

import sci.inf.Models.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements Runnable {

    void handleConnection() throws IOException {
        Socket connection = Server.getInstance().socket.accept();

        // start thread that adds new user, continue listening
        new Thread(new LoginHandler(connection)).start();
    }

    @Override
    public void run() {
        new Thread(new LogoutHandler()).start();
        while(Server.getInstance().isRunning) {
            try {
                handleConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class LoginHandler implements Runnable{
        Socket connection;

        public LoginHandler(Socket connection){
            this.connection = connection;
        }

        void sendNotifications(String username){
            try {
                Server.getInstance().sendNotificationToAll("NEW_LOGIN", username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                var reader = new DataInputStream(connection.getInputStream());
                String username = reader.readUTF();
                if(username == null) return;
                var user = new User(username, connection.getInetAddress().toString(), connection);
                Server.getInstance().users.add(user);
                user.logIn();

                // start new thread that notifies all users that user logged in
                new Thread(() -> { sendNotifications(username); }).start();
                System.out.println("New user connected: " + username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class LogoutHandler implements Runnable{

        @Override
        public void run() {
            while(Server.getInstance().isRunning){
                List<User> toRemove = new ArrayList<>();
                for(int i = 0 ; i < Server.getInstance().users.size(); i++){
                    User u = Server.getInstance().users.get(i);
                    if(!u.isOnline()) {
                        System.out.println("User is offline: " + u.username);
                        toRemove.add(u);
                    }
                }

                synchronized (Server.getInstance().users){
                    //remove users from server list
                    for(User i : toRemove) {
                        Server.getInstance().users.remove(i);
                    }
                }

                for(var u : toRemove) {
                    try {
                        Server.getInstance().sendNotificationToAll("NEW_LOGOUT", u.username);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                toRemove.clear();
            }
        }
    }
}
