package com.aether.client;

import org.joml.Vector2f;

import java.util.concurrent.ConcurrentHashMap;

public class ClientWorld {

    private final ConcurrentHashMap<String, Vector2f> playerPositions = new  ConcurrentHashMap<>();

    public void updatePlayer(float x, float y, String playerId){
        playerPositions.put(playerId, new Vector2f(x, y));
    }

    public void removePlayer(String playerId){
        playerPositions.remove(playerId);
    }

    public ConcurrentHashMap<String, Vector2f> getPlayerPositions(){
        return playerPositions;
    }

}
