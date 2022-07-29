import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class Connection extends Thread{
    String name;
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream is;
    Connection(Socket s)
    {
        socket = s;
        try{
            this.os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        }
        catch(Exception e){}
        try{
            Message m = ((Message)is.readObject());
            name = m.senderName;
            System.out.println(name);
        }
        catch(Exception e){System.out.println(e.getMessage());}
        start();
        
    }
    public void send(Message msg)
    {
        try{
            os.writeObject(msg);
        }
        catch(Exception e){System.out.println(e.getMessage());}
    }
    public void run() {
        Message msg;
        while(true)
        {
            try{
                msg = (Message)is.readObject();
                ServerProcess.q.add(msg);
            }
            catch(Exception e){System.out.println(e.getMessage());}
        }
    }
}

class ServerProcess extends Thread{
    static volatile Queue<Message> q = new LinkedList<>();
    ServerProcess()
    {
        super();
        start();
    }
    public void run() {
        while(true)
        {
            if(!q.isEmpty())
            {
                Message temp = q.remove();
                for(Connection connection : Server.s)
                {
                    if(connection!=null && !connection.name.equals(temp.senderName))
                    {
                        connection.send(temp);
                    }
                }
            }
        }
    }

}

public class Server {
    static Set<Connection> s = new HashSet<Connection>();
    public static void main(String[] args) throws Exception {
        new ServerProcess();

        ServerSocket ss = new ServerSocket(5000);
        while(true){
            Socket socket = ss.accept();
            System.out.println("New Connection");
            s.add(new Connection(socket));
        }
    }

}
