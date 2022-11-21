import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private ChessBoardUI chess = new ChessBoardUI();
    public ChessBoardUI getChess(){
        return chess;
    }
    private Socket socket = null;
    private String name;
    private DataInputStream ds;
    private DataOutputStream ps;
    private Listener listener;

    public Client(){
        init();
    }

    public void init(){
        chess.drawChessboard();
        for(int i = 0;i<3;i++){
            for(int j = 0;j<3;j++){
                chess.getModel().getChess()[i][j] = 2;
            }
        }
        try {
            socket = new Socket(InetAddress.getLocalHost(),3000);
            ps = new DataOutputStream(socket.getOutputStream());

            listener = new Listener(socket, name, this);
            listener.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
