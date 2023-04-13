package com.hasan.multiplayer.projects.fighter.server;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class App 
{
    public static void main( String[] args ){
        JSONObject serverStartObject = new JSONObject()
            .put("serverName", "mainServer")
            .put("maxPlayers", 4)
            .put("port", 5050);
        try {
            new server(serverStartObject);
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
