import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private final List<GameRoom> rooms = new ArrayList<>();
    private int nextRoomId = 1;
    public synchronized GameRoom assign(Player p){
        for(GameRoom r: rooms){
            if(!r.started && r.players.size()<4){
                r.players.add(p);
                if(r.players.size()==4){
                    r.started=true;
                }
                p.room = r;
                return r;
            }
        }
        GameRoom r = new GameRoom(nextRoomId);
        nextRoomId++;
        r.host = p;
        r.players.add(p);
        p.room = r;
        rooms.add(r);
        return r;
    }
    public synchronized void removePlayer(Player p){
        if(p.room==null)
            return;
        GameRoom r = p.room;
        r.players.remove(p);
        if(r.players.size()==0){
            rooms.remove(r);
        } else if (r.host==p) {
            r.host = r.players.get(0);
        }
        p.room = null;
    }
}
