package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.logger.statusType;
import com.hasan.multiplayer.projects.flighter.game.enums.multiplayer.serverMessageReturnType;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.personalLobby.miniGameLobby;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

public class rpsGameLobby extends miniGameLobby {

    rps application;
    String[] serverReq = { "connectedEntities?", "finish-Initialize" };

    public rpsGameLobby(gamePanel gp, rps application, int port) throws IOException {
        super(gp, application, port);
        this.application = application;
        initialize();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initialize() {
        JSONObject readData = (JSONObject) readServer(serverMessageReturnType.jsonObject);
        if (readData.has("initialize-server") && readData.getBoolean("initialize-server")) {
            boolean initializeServer = true;
            boolean initializeClient = false;
            // Initialize Server
            while (initializeServer) {
                // Get Requested Item
                JSONObject requested = (JSONObject) readServer(serverMessageReturnType.jsonObject);
                String requestedItem = requested.getString("request");
                // process Request
                switch (requestedItem) {
                    case "username" -> {
                        writeServer(new JSONObject().put("username", gp.player.name));
                        logger(statusType.response, new JSONObject().put("username?", gp.player.name)); // Logger
                    }
                    case "clientID?" -> {
                        writeServer(new JSONObject().put("clientID?", gp.multiplayer.clientID));
                        logger(statusType.response, new JSONObject().put("clientID?", gp.multiplayer.clientID)); // Logger
                    }
                    case "initialize-Client" -> {
                        initializeServer = false;
                        initializeClient = true;
                        break;
                    }
                }
            }
            // Initialze Client
            if (initializeClient) {
                // Go through requests and request from server
                for (String request : serverReq) {
                    // Request Requirement
                    writeServer(new JSONObject().put("request", request));
                    // Finish Initialize
                    logger(statusType.statusUpdate, "finishing-Initialize");
                    if (request == "finish-Initialize") {
                        initializeClient = false;
                        break;
                    }
                    logger(statusType.requested, new JSONObject().put("request", request)); // Logger
                    // get Server Response
                    JSONObject reply = (JSONObject) readServer(serverMessageReturnType.jsonObject);
                    // check and apply
                    if (reply.has(request)) {
                        logger(statusType.response, "server responded -> " + reply); // Log
                        switch (request) {
                            case "connectedEntities?" -> {
                                JSONArray players = reply.getJSONArray("connectedEntities?");
                                if (!players.isEmpty() && players.getInt(0) != gp.multiplayer.clientID) {
                                    // Add player to the connected Player List in backWorks
                                    tools.addPlayerMini(application.backWorks.connectedPlayers,
                                            tools.findByID(gp.players, players.getInt(0)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
