package fr.lernejo.navy_battle;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class Player {
    private final List<String> bingo = new ArrayList<String>();
    private final String target, id;
    private final int port;
    private final HttpClient httpClient;
    public Player(int port, String target, String id, HttpClient httpClient){
        this.port=port;
        this.target=target;
        this.id=id;
        this.httpClient=httpClient;
        for (int i=0; i < 10; i++){
            for (int j=0; j < 10; j++){
                this.bingo.add( Character.toString((char) 'A'+i) + Character.toString((char) '0' + j));
                shuffle(this.bingo);
            }
        }
    }
    public String play(){
        String turn = this.bingo.get(0);
        this.bingo.remove(0);
        return turn;
    }
}
