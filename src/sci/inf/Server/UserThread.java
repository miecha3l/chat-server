package sci.inf.Server;

import sci.inf.Models.Message;
import sci.inf.Models.User;

import java.io.IOException;

public class UserThread implements Runnable {

    User user;
    boolean isActive = false;

    public UserThread(User user){
        this.user = user;
        isActive = true;
    }

    @Override
    public void run() {

        user.sendUserList(user.username);

        //actively check if user logged out
        new Thread(new StatusHandler()).start();

        //begin listening for incoming messages
        while(this.isActive && user.isOnline()){
            try {
                if(user.in.available() > 0){
                    var msg = user.in.readUTF();
                    System.out.println("Incoming msg: " + msg);

                    var parsed = Message.parseStr(msg);
                    if(parsed != null) Server.getInstance().incomingMessages.push(parsed);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class StatusHandler implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(user.isOnline()) {
                try {
                    user.out.writeUTF("keep_alive");
                    Thread.sleep(5000);
                } catch (IOException e) {
                    System.out.println("User " + user.username + " logged out");
                    user.logOut();
                    isActive = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
