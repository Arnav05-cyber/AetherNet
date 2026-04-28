package com.aether.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameWorld {

    private final Map<String, PlayerData> players = new ConcurrentHashMap<>();

    public void updatePlayerPosition(String playerId, float x, float y) {
        players.put(playerId, new PlayerData(x,y));
        System.out.println("Updated player "+playerId+" position to x="+x+", y="+y);
    }

    public static class PlayerData{
        public float x,y;
        public PlayerData(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
