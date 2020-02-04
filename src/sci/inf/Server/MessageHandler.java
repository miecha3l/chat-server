package sci.inf.Server;

import sci.inf.Models.User;

import java.io.IOException;


public class MessageHandler implements Runnable {

    void fetchRequests(){
        while(Server.getInstance().incomingMessages.size() > 0){
            var msg = Server.getInstance().incomingMessages.pop();
            System.out.println("Fetched: " + msg.toString());

            if(msg.getReceiver().equals("SERVER")) handleServerDirectedRequest();
            else Server.getInstance().outgoingMessages.push(msg);
        }
    }

    void sendMessages() throws IOException {
        while(Server.getInstance().outgoingMessages.size() > 0){
            var msg = Server.getInstance().outgoingMessages.pop();

            System.out.print("Sending message: " + msg.toString() + " | to: ");
            var target = msg.getSender().contains("SERVER") ? msg.getSender().split("-")[1] : msg.getReceiver();
            System.out.println(target);

            synchronized (Server.getInstance().users){
                for(User u : Server.getInstance().users){
                    if(u.username.equals(target) && u.isOnline()) u.sendMessage(msg);
                }
            }
        }
    }

    private void handleServerDirectedRequest() {
        System.out.println("Handling server request...");
    }



    @Override
    public void run() {
        while(Server.getInstance().isRunning){
            try{
                fetchRequests();
                sendMessages();
            }
            catch(IOException exc){
                exc.printStackTrace();
            }
        }
    }
}
