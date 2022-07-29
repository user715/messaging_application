import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;

// class MyThread extends Thread{
//     MyThread()
//     {
//         super("MyThread");
//     }
//     public void run(){
//         BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
//         System.out.println("Enter Name :");
//         try{
//         Client.name = kb.readLine();
//         Client.send(null);
//         }
//         catch(Exception e){}
//         String body;
//         while(true)
//         {
//             try{
//                 body = kb.readLine();
//                 Client.send(body);
//             }
//             catch(Exception e){System.out.println(e.getMessage());}
//         }
//     }
// }
class DialogIP {  
    private static Dialog d;  
    DialogIP() {  
        Frame f= new Frame();  
        d = new Dialog(f , "Server IP", true);  
        d.setLayout(null);  
        TextField name = new TextField("Enter Server IP");
        name.setBounds(30,50,240,30);
        Button b = new Button ("OK");  
        b.setBounds(120,90,60,30);
        b.addActionListener ( new ActionListener()  
        {  
            public void actionPerformed( ActionEvent e )  
            {  
                Client.serverIP = name.getText();
                DialogIP.d.setVisible(false);  
            }  
        });  
        d.add(name);  
        d.add(b);   
        d.setBounds(250,180,300,140);
        d.setVisible(true);  
    }  
}

class DialogName {  
    private static Dialog d;  
    DialogName() {  
        Frame f= new Frame();  
        d = new Dialog(f , "Name", true);  
        d.setLayout(null);  
        TextField name = new TextField("Enter Your Name");
        name.setBounds(30,50,240,30);
        Button b = new Button ("OK");  
        b.setBounds(120,90,60,30);
        b.addActionListener ( new ActionListener()  
        {  
            public void actionPerformed( ActionEvent e )  
            {  
                Client.name = name.getText();
                Client.send(null);
                DialogName.d.setVisible(false);  
            }  
        });  
        d.add(name);  
        d.add(b);   
        d.setBounds(250,180,300,140);
        d.setVisible(true);  
    }  
}

class Client{
    static String serverIP;
    static String name;
    static ObjectOutputStream os;
    static ObjectInputStream is;
    public static void send(String s){
        Message m = new Message(name,s);
        try{
        os.writeObject(m);
        }
        catch(Exception e){System.out.println(e.getMessage());}
    }

    public static void main(String[] args) throws Exception {
        Frame f = new Frame();
        TextArea input = new TextArea("Enter Message");
        input.setBounds(50,400,600,60);
        Button send = new Button("Send");
        send.setBackground(Color.GREEN);
        send.setBounds(680,400, 100, 30);
        send.addActionListener(new ActionListener() {    
            public void actionPerformed (ActionEvent e) {   
                    Client.send(input.getText());
                    input.setText("");  
                }    
            });
        f.add(send);
        f.add(input);
        TextArea Messages = new TextArea("",0 ,0 ,TextArea.SCROLLBARS_VERTICAL_ONLY);
        Messages.setEditable(false);
        Messages.setBounds(50,50,700,300);
        f.add(Messages);
        f.setTitle("Messenger");
        f.setResizable(false);
        f.setSize(800,500);
        f.setLayout(null);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        Socket s;
        new DialogIP();
        try{
        s = new Socket(serverIP,5000);
        os = new ObjectOutputStream(s.getOutputStream());
        is = new ObjectInputStream(s.getInputStream());
        }
        catch(Exception e){System.exit(0);}
        
        // MyThread tr = new MyThread();
        // tr.start();

        
        new DialogName();
        Message t;
        while(true)
        {
            try{
                t = (Message)is.readObject();
                Messages.append("\n"+t.senderName+" : "+t.msg);
            }
            catch(Exception e){System.out.println(e.getMessage());}
        }
    }
}