package com.hasan.multiplayer.projects.flighter.game.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Graphics;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.enums.logger.statusType;
import com.hasan.multiplayer.projects.flighter.game.enums.multiplayer.serverMessageReturnType;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.rps;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers.objectDeleteHandler;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers.objectSpawnHandler;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers.objectUpdateHandler;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers.playerDeleteHandler;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers.playerSpawnHandler;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers.playerUpdateHandler;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

/**
 * multiplayer, is used to set the game online
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class multiplayer {
    public int clientID;
    public final String username;

    public gamePanel gp;

    public boolean gameStarted = false;

    final playerSpawnHandler playerSpawnHandler;
    final playerUpdateHandler playerUpdateHandler;
    final playerDeleteHandler playerDeleteHandler;
    final objectSpawnHandler objectSpawnHandler;
    final objectUpdateHandler objectUpdateHandler;
    final objectDeleteHandler objectDeleteHandler;

    public JSONArray _playerSpawns;
    public JSONArray _objectSpawns;

    final BufferedReader fromServer;
    final PrintWriter toServer;
    final Socket server;
    // Requred Initialze Assets
    final String[] requiredInitialAssets = { "clientID?", "initialPlayerSpawns?", "initialObjectSpawns?" };

    public multiplayer(String host, int port, gamePanel gp) throws UnknownHostException, IOException {
        server = new Socket(host, port);
        username = gp.player.name;
        this.toServer = new PrintWriter(server.getOutputStream(), true); // Initialize Server Writer
        this.fromServer = new BufferedReader(new InputStreamReader(server.getInputStream())); // Initialize Server
                                                                                              // Response Reader
        this.gp = gp;
        this.playerUpdateHandler = new playerUpdateHandler(gp, this);
        this.playerSpawnHandler = new playerSpawnHandler(gp, this);
        this.playerDeleteHandler = new playerDeleteHandler(gp, this);
        this.objectSpawnHandler = new objectSpawnHandler(gp, this);
        this.objectUpdateHandler = new objectUpdateHandler(gp, this);
        this.objectDeleteHandler = new objectDeleteHandler(gp, this);
        initializeClient();
    }

    private Object readServer(serverMessageReturnType returnType) {
        try {
            switch (returnType) {
                case jsonObject:
                    return new JSONObject(fromServer.readLine());
                case numbers:
                    return Double.parseDouble(fromServer.readLine());
                case string:
                    return fromServer.readLine();
                case jsonArray:
                    return new JSONArray(fromServer.readLine());
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println("[Multiplayer][readServer]: There was a problem reading the server's input");
        }
        return null;
    }

    private void writeServer(Object message) {
        toServer.println(message);
    }

    void logger(statusType statType, Object message) {
        statType.println(username, message);
    }

    /**
     * This Method is used to Initialize the client to the server and vice versa
     * 
     * @throws NumberFormatException
     * @throws IOException
     */
    private void initializeClient() throws NumberFormatException, IOException {
        writeServer("initialize-clientSide-Start"); // Initialize
        logger(statusType.requested, "Start Initialize Process"); // Logger
        logger(statusType.statusUpdate, "Initialization process started"); // Logger
        for (String request : requiredInitialAssets) {
            writeServer(new JSONObject().put("request", request)); // Request
            logger(statusType.requested, "Requesting -> " + request); // Logger
            JSONObject tempRequestHold = (JSONObject) readServer(serverMessageReturnType.jsonObject); // Response
            // Process Response
            if (tempRequestHold.has(request)) {
                logger(statusType.response, "Response -> " + tempRequestHold); // Logger
                switch (request) {
                    case "clientID?" -> {
                        clientID = tempRequestHold.getInt(request);
                    }
                    case "initialPlayerSpawns?" -> {
                        _playerSpawns = tempRequestHold.getJSONArray(request);
                        playerSpawnHandler.playerSpawn(_playerSpawns);
                        _playerSpawns.clear();
                    }
                    case "initialObjectSpawns?" -> {
                        _objectSpawns = tempRequestHold.getJSONArray(request);
                        objectSpawnHandler.objectSpawn(_objectSpawns);
                    }
                }
            }
        }
        writeServer(new JSONObject().put("request", "initialize-clientSide-Stop-StartServerSide?"));
        logger(statusType.statusUpdate, "Flipping initialization Process (now the Server Requests)"); // Logger
        boolean initialize = true;
        logger(statusType.statusUpdate, "Waiting for Server Requests"); // Logger
        while (initialize) {
            JSONObject tempHold = (JSONObject) readServer(serverMessageReturnType.jsonObject);
            // Return Client Requests
            if (tempHold.has("request")) {
                String tempRequest = tempHold.getString("request").trim();
                logger(statusType.requested, "Server has requested: " + tempRequest); // Logger
                // Return the Correct Requested Item
                switch (tempRequest) {
                    case "username?" -> {
                        writeServer(new JSONObject().put("username?", username));
                        logger(statusType.response, new JSONObject().put("username?", username)); // Logger
                    }
                    case "mySpawn?" -> {
                        writeServer(new JSONObject().put("mySpawn?", gp.player.getSpawn()));
                        logger(statusType.response, new JSONObject().put("spawnObject?", gp.player.getSpawn())); // Logger
                    }
                    case "finish-Initialize" -> {
                        initialize = false;
                        logger(statusType.statusUpdate, "Initialization Finished");
                    }
                }
            }
        }
        this.gameStarted = true;
        logger(statusType.statusUpdate, "Online Game Started");
    }

    public void update() {
        if (gameStarted) {
            JSONObject updateToSent = new JSONObject();
            if (gp.player != null) {
                JSONObject gameUpdate = (JSONObject) readServer(serverMessageReturnType.jsonObject);
                gameUpdate = gameUpdate.getJSONObject("gameUpdate");
                // Check for Updates and their Existance
                if (gameUpdate.has("playerSpawn")) {
                    JSONArray playerSpawn = gameUpdate.getJSONArray("playerSpawn");
                    playerSpawnHandler.playerSpawn(playerSpawn);
                }
                if (gameUpdate.has("playerUpdates")) {
                    JSONArray playerUpdates = gameUpdate.getJSONArray("playerUpdates");
                    playerUpdateHandler.playerUpdate(playerUpdates);
                }
                if (gameUpdate.has("playerDelete")) {
                    JSONArray playerDelete = gameUpdate.getJSONArray("playerDelete");
                    playerDeleteHandler.playerDelete(playerDelete);
                }
                if (gameUpdate.has("objectSpawns")) {
                    JSONArray objectSpawns = gameUpdate.getJSONArray("objectSpawns");
                    objectSpawnHandler.objectSpawn(objectSpawns);
                }
                if (gameUpdate.has("objectUpdate")) {
                    JSONArray objectUpdates = gameUpdate.getJSONArray("objectUpdate");
                    objectUpdateHandler.updateObject(objectUpdates);
                }
                if (gameUpdate.has("objectDeletes")) {
                    JSONArray objectDelete = gameUpdate.getJSONArray("objectDeletes");
                    objectDeleteHandler.objectDelete(objectDelete);
                }
                if (gameUpdate.has("miniGameServer") && !gameUpdate.getJSONObject("miniGameServer").isEmpty()){
                    System.out.println(gameUpdate.getJSONObject("miniGameServer"));
                    boolean spinUp = gameUpdate.getJSONObject("miniGameServer").getBoolean("spunUp");

                    if (spinUp){
                        gp.hud.addNewMidNotification(tools.UUIDCreator(), "mini-server Spun up", 5, "[miniGame]", notifyType.success);
                    }
                }

                if (gp.keyH.multipleyerThrow) {
                    superObject so = new rps(gp, gp.player, false);
                    so.worldPosition.setLocation(gp.player.worldPosition);
                    updateToSent.put("objectSpawn", new JSONArray().put(so.getSpawn()));
                    gp.keyH.multipleyerThrow = false;
                    gp.gameObjects.add(so);

                    updateToSent.put("miniGameServer", new JSONObject()
                                        .put("name", "RPS")
                                        .put("maxEntities", 2)
                                        .put("port", 5621));
            
                } else {
                    updateToSent.put("objectSpawn", new JSONArray());
                }
                // if (gp.keyH.multipleyerThrow && gp.gameObjects.size() > 0) {
                //     superObject so = gp.gameObjects.get(2);
                //     updateToSent.put("objectDelete", new JSONArray().put(so.getDelete()));
                //     gp.keyH.multipleyerThrow = false;
                // } else {
                //     updateToSent.put("objectDelete", new JSONArray());
                // }
                JSONArray updateJArray = new JSONArray();
                // send spwned Object Updates
                gp.gameObjects.stream()
                        .filter((updateObject) -> updateObject.spawnedBy == clientID)
                        .forEach(object -> updateJArray.put(object.getUpdate()));

                        updateToSent.put("objectUpdate", updateJArray);
                updateToSent.put("playerUpdate", gp.player.getUpdate());

                writeServer(new JSONObject().put("gameUpdate", updateToSent));
            }
        }
    }

    public void draw(Graphics g) {
        g.drawString("ID: " + clientID, 10, 15);
    }
}
