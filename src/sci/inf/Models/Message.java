package sci.inf.Models;

public class Message {
    public Message(String s, String r, String m){
        this.sender = s;
        this.receiver = r;
        this.message = m;
    }

    String sender;
    String receiver;
    String message;

    @Override
    public String toString() {
        return sender + ";" + receiver + ";" + message;
    }

    public static Message parseStr(String req){
        var arr = req.split(";");
        try{
            return new Message(arr[0], arr[1], arr[2]);
        }
        catch(Exception e){
            return null;
        }
    }

    public static Message updateMessage(String message, String receiver, String type) {
        return new Message("SERVER-" + receiver, type, message);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
