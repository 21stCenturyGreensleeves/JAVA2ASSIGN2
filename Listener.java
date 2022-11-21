import java.io.*;
import java.net.Socket;


public class Listener extends Thread {
  private Socket socket;
  private int BOUND = 90;
  private String name;
  private String enemy;
  private Client client;
  private int color;
  private DataOutputStream ps;
  private DataInputStream ds;
  int x;
  int y;

  public Listener(Socket s,String name,Client client){
      this.socket = s;
      this.name = name;
      this.client = client;
      if(socket!= null){
        try {
          ds = new DataInputStream(socket.getInputStream());
          ps = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void run(){
        String line = "";
        while(true){
            try {
                line = ds.readUTF();
            } catch (IOException e) {
                System.out.println("服务器寄了\n请退出");

                return;
            }
            String[] arr = line.split("/");
            if (arr[0].equalsIgnoreCase("WARN")){
                if (arr[1].equals(enemy)){
                    System.out.println("对方已跑路，拜拜");
                    try {
                        ps.writeUTF("QUITME/"+name);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else
            if (arr[0].equalsIgnoreCase("WAITING")){
                System.out.println("I LL BEEN WAITING FOR LOVE,WAITING FOR LOVE,TO COME AROUND");
            }else
            if(arr[0].equalsIgnoreCase("MATCH")){
                name = arr[3];
                //arr[1]是对方的int，arr【2】是棋子的颜色
                enemy = arr[1];
                if(arr[2].equalsIgnoreCase("0")){
                    System.out.println("你是XP");
                    System.out.println("XP为");
                    System.out.println(name);
                    System.out.println("OP为");
                    System.out.println(enemy);


                    color = 0;
                    chessMove();
                }else if(arr[2].equalsIgnoreCase("1")){
                    System.out.println("你是OP");
                    System.out.println("OP为");
                    System.out.println(name);
                    System.out.println("XP为");
                    System.out.println(enemy);


                    color = 1;
                }
            }else if(arr[0].equalsIgnoreCase("MSG")){
                String name = arr[1];
                readMotion(name);
                if(color == 1){
                    if(judgeWinner(0)){
                        System.out.println("你输了\n游戏结束，请关闭界面");
                        try {
                            send("QUIT/"+enemy);


                            return;

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    }else if(judgeTie()){
                        System.out.println("平了\n游戏结束，请关闭界面");
                        try {
                            send("QUIT/"+enemy);


                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }else if (color == 0){
                    if(judgeWinner(1)){
                        System.out.println("你输了\n游戏结束，请关闭界面");
                        try {
                            send("QUIT/"+enemy);


                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else if(judgeTie()){
                        System.out.println("平了\n游戏结束，请关闭界面");
                        try {
                            send("QUIT/"+enemy);


                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                chessMove();
            }
        }
    }
    public boolean judgeTie(){
        if(client.getChess().getModel().judgeTie()){
            return true;
        }return false;
    }
    public boolean judgeWinner(int color){
        if(client.getChess().getModel().judgeLine(color)||
        client.getChess().getModel().judgeDiagonal(color)){
            return true;
        }
        return false;
    }
    public void endGame(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
    public void readMotion(String value){
        String[] arr = value.split(" ");
        int x = Integer.parseInt(arr[0]);
        int y = Integer.parseInt(arr[1]);
        if(color == 0){
            client.getChess().getModel().getChess()[x][y] = 1;
            client.getChess().drawChess(x, y, 1);
        }else{
            client.getChess().getModel().getChess()[x][y] = 0;
            client.getChess().drawChess(x,y,0);
        }
    }

    public void chessMove(){
        client.getChess().getRoot().setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);//通过点击位置获取对应棋盘数组上的位置
            try {
                client.getChess().drawChess(x,y,color);
                send("MSG/"+enemy+"/"+x+" "+y);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            client.getChess().getModel().getChess()[x][y] = color;
            if(color == 1){
                if(judgeWinner(1)){
                    System.out.println("你赢了\n游戏结束，请关闭界面");
                    try {
                        send("QUIT/"+enemy);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else if (judgeTie()){
                    System.out.println("平了\n游戏结束，请关闭界面");
                    try {
                        send("QUIT/"+enemy);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else {
                if(judgeWinner(0)){
                    System.out.println("你赢了\n游戏结束，请关闭界面");
                    try {
                        send("QUIT/"+enemy);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else if (judgeTie()){
                    System.out.println("平了\n游戏结束，请关闭界面");
                    try {
                        send("QUIT/"+enemy);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            client.getChess().getRoot().setOnMouseClicked(null);
        });
    }
    public void send(String msg) throws IOException {
        ps.writeUTF(msg);
        ps.flush();
    }
}
