import java.io.Serializable;

public class Message implements Serializable {
    String senderName;
    String msg;
    Message(String senderName,String msg){
        this.senderName = senderName;
        this.msg = msg;
    }
}
