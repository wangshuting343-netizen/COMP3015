import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    public int roomId;
    public ServerEngine engine = new ServerEngine();
    public List<Player> players = new ArrayList<>();
    public Player host;
    public boolean started = false;
    public final Object lock = new Object();
    public int currentPlayer = 0;
    public int moveLeft = 4;

    public GameRoom(int roomId){
        this.roomId = roomId;
    }
}
