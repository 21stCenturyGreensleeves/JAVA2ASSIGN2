import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private Socket socket = null;
    private DataInputStream is;
    private DataOutputStream ps;
    private List<Player> player;
    private Player lastPlayer = null;
//    private List<Composition> compositions;
    private Link link;
//    private part part;
    class Link extends Thread{
        public void run(){
            while(true){
                if(player.size() < 10){
                    try {
                        socket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("PLAYER NUMBER:"+player.size());
                    for (Player player1:player){
                        System.out.println(player1.name);
                    }
                    Player player1 = new Player(socket);
                    synchronized (new Link()){
                        player.add(player1);
                    }
                    for(int i = 0;i<player.size();i++){
                        System.out.println(player.get(i).name);
                    }
                    Player black = null;
                    Player white = null;
                    black = lastPlayer;
                    white = player1;
                    lastPlayer = player1;
                    System.out.println("PLAYERS NAME: "+player1.name);
                    player1.start();
                    if(player.size()%2 == 0){
                        black.send("MATCH/"+white.name+"/1/"+black.name);
                        System.out.println("X"+white.name);
                        System.out.println("O"+black.name);
                        white.send("MATCH/"+black.name+"/0/"+white.name);
                    }else {
                        player1.send("WAITING");
                        System.out.println(player1.name+" WAITING");
                    }
                }
                else{
                    try{
                        Thread.sleep(400);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Player extends Thread{
        Socket s;
        int name;
        InetAddress ip;
        DataOutputStream ps;
        DataInputStream ds;
        public Player(Socket socket){
            this.s = socket;

            try {
                ps = new DataOutputStream(s.getOutputStream());
                ds = new DataInputStream(s.getInputStream());
                this.ip = s.getLocalAddress();
                this.name = (int)(Math.random()*10000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            while(true){
                String line = null;
                try {
                    line = ds.readUTF();
                    System.out.println("THIS IS SERVERS LINE");
                    System.out.println(line);
                } catch (IOException e) {
                    System.out.println(name+" is not connected");
                    player.remove(this);
                    System.out.println(player.size());
                    for(int i = 0;i<player.size();i++){
                        if(player.get(i).name != name){
                            try {
                                player.get(i).ps.writeUTF("WARN/"+name);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    e.printStackTrace();
                    stop();
                }
                if(line != null){
                    String[] arr = line.split("/");//[类型/敌方id/坐标][类型/敌方id/颜色]【退出/敌方id】
                    String type = arr[0];
                    if(type.equalsIgnoreCase("MSG")){
                        int enemyName = Integer.parseInt(arr[1]);
                        String pos = arr[2];
                        for (Player player1:player){
                            if(player1.name == enemyName){
                                player1.send("MSG/"+pos);
                            }
                        }
                    }else if(type.equalsIgnoreCase("QUIT")){
                        int enemyName = Integer.parseInt(arr[1]);
                        System.out.println(this.name);
                        for (Player player1:player){
                            if(player1.name == enemyName){
                                player1.send("QUIT");
                            }
                        }
                        diconnect(this);
                        return;
                    }else if(type.equalsIgnoreCase("QUITME")){
                        int name = Integer.parseInt(arr[1]);
                        for(Player player1:player){
                            if(player1.name == name){
                                player1.send("QUIT");
                            }
                        }diconnect(this);
                        return;
                    }
                }
            }
        }
        public synchronized void diconnect(Player player1){
            player1.send("QUIT"+player1.name);

            player.remove(player1);
        }
        public void send(String msg){
            try {
                ps.writeUTF(msg);
                ps.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

//    class Composition{
//        private Player black;
//        private Player white;
//        public Composition(Player b,Player w){
//            black = b;
//            white = w;
//        }
//    }
    public void init(){
        port = 3000;
        player = new Vector<>();
//        compositions = new Vector<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        link = new Link();
        link.start();
    }
}
