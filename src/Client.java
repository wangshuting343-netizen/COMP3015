import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    private String ipAddress;
    private int tcpPort;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String ipAddress, int tcpPort){
        this.ipAddress=ipAddress;
        this.tcpPort=tcpPort;

    }

    public void disconnect(){
        try {
            in.close();
            out.close();
            socket.close();
        }catch (IOException ex){
            System.out.println("Bye bye~");
        }
        in = null;
        out = null;
        socket = null;
    }

    public void connect() throws IOException {
        socket = new Socket(ipAddress, tcpPort);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        Thread receiveThread = new Thread(()->{
            try {
                while (true) {
                    receive();
                }
            }catch (IOException ex){
                System.out.println("Disconnected!");
                System.exit(-1);
            }
        });
        receiveThread.start();
    }

    public void send(int dataType, String message) throws IOException {
        int len = message.length();

        out.writeInt(dataType);

        out.writeInt(len);
        out.write(message.getBytes(), 0, len);
        out.flush();
    }

    public void receive() throws IOException{
        int dataType = in.readInt();
        System.out.println(">>> [5] 客户端收到消息类型: " + dataType);
        switch(dataType){
            case GameEngine.PUZZLE:
                handleReceivePuzzleData();
                break;
            case GameEngine.PLAYER_INFO:
                handleReceivePlayerInfo();
                break;
        }
    }
    public void handleReceivePlayerInfo() throws IOException {
        int len = in.readInt();
        byte[] nameBytes = new byte[len];
        in.readFully(nameBytes);
        String name = new String(nameBytes);
        int score = in.readInt();
        System.out.println("Score of somebody!"+name+" "+score);
    }

    public void handleReceivePuzzleData() throws IOException{
        int width = in.readInt();
        int height = in.readInt();
        int[] puzzle = new int[width * height];

        for(int i=0; i<width*height;i++){
            puzzle[i]=in.readInt();
        }

        boolean isGameOver = in.readBoolean();
        System.out.println("Received: " + Arrays.toString(puzzle));
        GameEngine engine = GameEngine.getInstance();

        engine.board = puzzle;
        engine.gameOver = isGameOver;
    }

//    public static void main(String[] args) throws IOException{
//        Client client = new Client("127.0.0.1", 12345);
//        client.connect();
//        client.send("I am here!!!");
//    }
}
