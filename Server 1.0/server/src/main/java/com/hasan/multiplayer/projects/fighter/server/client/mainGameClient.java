package com.hasan.multiplayer.projects.fighter.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.fighter.server.server;
import com.hasan.multiplayer.projects.fighter.server.enums.logger.statusType;
import com.hasan.multiplayer.projects.fighter.server.enums.multiplayer.serverMessageReturnType;
import com.hasan.multiplayer.projects.fighter.server.miniGame.miniGameServer;

public class mainGameClient extends Thread {
    boolean gameUpdateNotRecived = false;
    public boolean _newPlayer = true;
    final server mainServerClass;
    // Client Reader and Writer
    final BufferedReader fromClient;
    final PrintWriter toClient;
    // Client ID/Username
    public final int clientID;
    public String username;
    // Socket
    final Socket clientSocket;
    List<mainGameClient> connectedClients;
    // Requred Initialze Assets
    final String[] requiredInitialAssets = { "username?", "mySpawn?" };
    // Player Game Update
    public JSONObject _mySpawnObject = new JSONObject(); // This is my player Spawn Object
    public JSONArray _myObjectSpawns = new JSONArray(); // This is a collective List of all Objects spawned by this
                                                        // client
    public JSONArray _myObjectDeletes = new JSONArray(); // This is a collective list of all Objects deleted by the
                                                         // client
    public JSONObject _deleteSpawnObject = new JSONObject();
    public JSONObject _playerUpdate = new JSONObject();
    public JSONArray _objectSpawn = new JSONArray();
    public JSONArray _objectUpdate = new JSONArray();
    public JSONArray _objectDelete = new JSONArray();
    public JSONArray _projectileUpdate = new JSONArray();
    public JSONObject _castedAbility = new JSONObject();
    public JSONObject _miniGameData = new JSONObject();
    public JSONObject _miniGameServer = new JSONObject();
    public JSONArray playerDelete = new JSONArray();

    final ExecutorService concurrentRunners; // Executoner Thread Pool

    public mainGameClient(Socket clientSocket, server mainServerClass) throws IOException {
        this.clientSocket = clientSocket;
        this.mainServerClass = mainServerClass;
        this.clientID = clientSocket.getPort();
        this.concurrentRunners = Executors.newFixedThreadPool(1); // Create a Fixed Pool of Clients
        logger(statusType.spacer, null); // Spacer
        logger(statusType.statusUpdate, "Client Connected"); // Logger
        // Client Talks
        {
            this.fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.toClient = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        {
            if (readClient(serverMessageReturnType.string).equals("initialize-clientSide-Start")) {
                logger(statusType.requested, "Client has requested to start intialization"); // Logger
                initializeClient();
            }
        }
    }

    /**
     * Logger, pretty obvious what it does
     * 
     * @param statType The Type of Status
     * @param message  The Message
     */
    void logger(statusType statType, Object message) {
        statType.println(clientID, message);
    }

    /**
     * This method is used to Initialize client onto the server, this method first
     * gives the client and then flips and then requests the clint for it's data
     */
    void initializeClient() {
        logger(statusType.statusUpdate, "Initializing Client"); // Logger
        boolean initialize = true;
        while (initialize) {
            JSONObject tempHold = (JSONObject) readClient(serverMessageReturnType.jsonObject);
            // Return Client Requests
            if (tempHold.has("request")) {
                String tempRequest = tempHold.getString("request").trim();
                logger(statusType.requested, "Client has requested: " + tempRequest); // Logger
                // Return the Correct Requested Item
                switch (tempRequest) {
                    // Return Client ID
                    case "clientID?" -> {
                        writeClient(new JSONObject().put(tempRequest, clientID));
                        logger(statusType.response, new JSONObject().put(tempRequest, clientID)); // Logger
                    }
                    case "initialPlayerSpawns?" -> {
                        JSONArray playerSpawns = new JSONArray();
                        if (mainServerClass.clientList.size() > 0) {
                            mainServerClass.clientList.forEach(client -> {
                                playerSpawns.put(client._mySpawnObject);
                            });

                            writeClient(new JSONObject().put("initialPlayerSpawns?", playerSpawns));
                            logger(statusType.response, new JSONObject().put("initialPlayerSpawns?", playerSpawns)); // Logger
                        } else {
                            writeClient(new JSONObject().put("initialPlayerSpawns?", playerSpawns));
                            logger(statusType.response,
                                    new JSONObject().put("initialPlayerSpawns?", playerSpawns) + ", no players"); // Logger
                        }
                    }
                    case "initialObjectSpawns?" -> {
                        JSONArray objectSpawns = new JSONArray();
                        mainServerClass.clientList.forEach(client -> {
                            objectSpawns.put(client._myObjectSpawns);
                        });

                        writeClient(new JSONObject().put("initialObjectSpawns?", objectSpawns));
                        logger(statusType.response, new JSONObject().put("initialObjectSpawns?", objectSpawns)); // Logger
                    }
                    // Close Client Side Initialize
                    case "initialize-clientSide-Stop-StartServerSide?" -> {
                        initialize = false; // Set Initialize False
                    }
                }
            }
            // Create Delete Object
            _deleteSpawnObject = new JSONObject()
                    .put("deleteSpawnID", clientID);
        }
        logger(statusType.statusUpdate, "Flipping initialization Process (now the Server Requests)"); // Logger
        // Request Items from Client
        for (String request : requiredInitialAssets) {
            {
                writeClient(new JSONObject().put("request", request));
                logger(statusType.requested, "Requesting -> " + request); // Logger
                JSONObject tempResponseHold = (JSONObject) readClient(serverMessageReturnType.jsonObject);
                logger(statusType.response, "Response -> " + tempResponseHold); // Logger
                if (tempResponseHold.has(request)) {
                    // Catch the Response
                    switch (request) {
                        case "username?" -> {
                            username = tempResponseHold.getString(request);
                            this.setName(username);
                        }
                        case "mySpawn?" -> {
                            _mySpawnObject = tempResponseHold.getJSONObject(request);
                            _mySpawnObject.remove("ID");
                            _mySpawnObject.put("ID", clientID);
                        }
                    }
                }
            }
        }
        writeClient(new JSONObject().put("request", "finish-Initialize"));
        logger(statusType.requested, "Finished Initialization"); // Logger
        this.start();
    }

    void resetableVariables (){
        _miniGameServer = new JSONObject();
    }

    /**
     * This is the Game Update Loop
     */
    public synchronized void run() {
        while (true) {
            // Update Client List
            this.connectedClients = mainServerClass.clientList;
            // Create Return gameObject
            {
                gameUpdate();
            }
            {
                processGameUpdate();
            }
            if (gameUpdateNotRecived) {
                break;
            }
            _newPlayer = false;
        }
    }

    /**
     * {@code gameUpdate()} goes through each goes through all <strong>connected
     * clients</strong>, and retrives their <strong>extracted</strong> updates;
     * <ol>
     * <li>{@code playerSpawn}</li>
     * <li>{@code playerUpdates}</li>
     * <li>{@code objectSpawns}</li>
     * <li>{@code objectUpdates}</li>
     * <li>{@code objectDelete}</li>
     * <li>{@code projectileUpdates}</li>
     * <li>{@code castedUpdates}</li>
     * </ol>
     * Once all the <i>*other client</i> <strong>extracted</strong> data is copied
     * on to temporary method variables, they are then packed using a
     * {@code JSONObject gameUpdateRTNFinal} as <i>gameUpdates</i> and sent to the
     * <strong>Client</strong>.
     */
    private void gameUpdate() {
        // Temp Variables for Items to be sent
        JSONArray playerSpawn = new JSONArray();
        JSONArray playerUpdates = new JSONArray();
        JSONArray objectSpawns = new JSONArray();
        JSONArray objectUpdates = new JSONArray();
        JSONArray objectDelete = new JSONArray();
        JSONArray projectileUpdates = new JSONArray();
        JSONArray castedAbility = new JSONArray();
        JSONArray miniGameData = new JSONArray();
        // This loop is to loop through all clients, so all clients can get this clients
        // Updates
        for (mainGameClient player : connectedClients) {
            // Aslong as 'player clientID' is not the same as this client perform the
            // information feed
            if (player.clientID != clientID) {
                if (!player._playerUpdate.isEmpty())
                    playerUpdates.put(player._playerUpdate);
                if (!player._objectSpawn.isEmpty()) {
                    objectSpawns.put(player._objectSpawn);
                    System.out.println(_objectSpawn);
                }
                if (!player._objectUpdate.isEmpty())
                    objectUpdates.put(player._objectUpdate);
                if (!player._objectDelete.isEmpty())
                    objectDelete.put(player._objectDelete);
                if (!player._projectileUpdate.isEmpty())
                    projectileUpdates.put(player._projectileUpdate);
                if (!player._castedAbility.isEmpty())
                    castedAbility.put(player._castedAbility);
                // Check if the player is new
                if (player._newPlayer) {
                    playerSpawn.put(player._mySpawnObject);
                }
                //// If Minigame Data needs to be Sent
                //if (!player._miniGameData.isEmpty()) {
                //    JSONObject tempMinigameData = player._miniGameData;
                //    // get all the recipients of the Minigame Data
                //    JSONArray recipients = tempMinigameData.getJSONArray("recipients");
                //    for (int index = 0; index != recipients.length(); index++) {
                //        int recipientID = recipients.getInt(index);
                //        if (recipientID == clientID) {
                //            miniGameData.put(tempMinigameData);
                //            break;
                //        }
                //    }
                //}
            }
        }
        // Put Everything into one game Update
        JSONObject gameUpdateRTNTemp = new JSONObject();
        gameUpdateRTNTemp.put("playerSpawn", playerSpawn);
        gameUpdateRTNTemp.put("playerUpdates", playerUpdates);
        gameUpdateRTNTemp.put("playerDelete", playerDelete);
        gameUpdateRTNTemp.put("objectSpawns", objectSpawns);
        gameUpdateRTNTemp.put("objectUpdates", objectUpdates);
        gameUpdateRTNTemp.put("objectDeletes", objectDelete);
        gameUpdateRTNTemp.put("projectileUpdates", projectileUpdates);
        gameUpdateRTNTemp.put("castedAbility", castedAbility);
        gameUpdateRTNTemp.put("miniGameData", miniGameData);
        gameUpdateRTNTemp.put("miniGameServer", _miniGameServer);
        JSONObject gameUpdateRTNFinal = new JSONObject()
                .put("gameUpdate", gameUpdateRTNTemp);
        // Return gameupdate to client
        writeClient(gameUpdateRTNFinal);
        resetableVariables();
    }

    /**
     * {@code processGameUpdate()} as the name says, <strong>processes</strong> and
     * <strong>recives</strong> the {@code gameUpdate}, this method is used to
     * seperate and retrieve game objects;
     * <ol>
     * <li>{@code playerUpdate}</li>
     * <li>{@code objectSpawn}</li>
     * <li>{@code objectUpdate}</li>
     * <li>{@code objectDelete}</li>
     * <li>{@code projectileUpdate}</li>
     * <li>{@code castedAbility}</li>
     * </ol>
     * These objects are saved on the clients update variables, for later retrieval
     * in {@code gameUpdate()}.
     */
    private void processGameUpdate() {
        // Read gameUpdate
        JSONObject gameUpdate = (JSONObject) readClient(serverMessageReturnType.jsonObject);
        // Stop the Client Connection
        if (gameUpdate == null) {
            logger(statusType.statusUpdate, "The gameUpdate was null");
            gameUpdateNotRecived = true;
            return;
        }
        // Extract updates, spawns, casts off game updates
        if (gameUpdate.has("gameUpdate")) {
            gameUpdate = gameUpdate.getJSONObject("gameUpdate");
            if (gameUpdate.has("playerUpdate")) {
                _playerUpdate = gameUpdate.getJSONObject("playerUpdate");
            }
            if (gameUpdate.has("objectSpawn")) {
                _objectSpawn = gameUpdate.getJSONArray("objectSpawn");
                _myObjectSpawns.putAll(_objectSpawn);
                for (mainGameClient client : connectedClients) {
                    if (client.clientID != clientID)
                        client._objectSpawn.putAll(_objectSpawn);
                }
            }
            if (gameUpdate.has("objectUpdate")) {
                _objectUpdate = gameUpdate.getJSONArray("objectUpdate");
            }
            if (gameUpdate.has("objectDelete")) {
                _objectDelete = gameUpdate.getJSONArray("objectDelete");
                for (mainGameClient client : connectedClients) {
                    if (client.clientID != clientID)
                        client._objectDelete.putAll(_objectDelete);
                }
            }
            if (gameUpdate.has("projectileUpdate")) {
                _projectileUpdate = gameUpdate.getJSONArray("projectileUpdate");
            }
            if (gameUpdate.has("castedAbility")) {
                _castedAbility = gameUpdate.getJSONObject("castedAbility");
            }
            if (gameUpdate.has("minigameData")) {
                _miniGameData = gameUpdate.getJSONObject("minigameData");
            }
            /**
             * If the client requests to start up a Mini-Game Server, Initialize a miniGameServer server 
             */
            if (gameUpdate.has("miniGameServer")) {
                logger(statusType.statusUpdate, "Client Requested to Loadup a Mini Server");
                JSONObject temp = gameUpdate.getJSONObject("miniGameServer");
                logger(statusType.statusUpdate, "server Info ->" + temp);
                logger(statusType.statusUpdate, "Attempting Start of Server");
                try {
                    miniGameServer mgs = new miniGameServer(temp, this, connectedClients);
                    mainServerClass.miniGameServers.add(mgs);
                    concurrentRunners.execute(mgs);
                    _miniGameServer = new JSONObject().put("spunUp", true);
                } catch (BindException portBoundedExeption) {
                    logger(statusType.statusUpdate, "Server Port Bound Exception");
                    _miniGameServer = new JSONObject().put("spunUp", false);
                }

            }
        }
    }

    private Object readClient(serverMessageReturnType returnType) {
        try {
            switch (returnType) {
                case jsonObject:
                    return new JSONObject(fromClient.readLine());
                case numbers:
                    return Double.parseDouble(fromClient.readLine());
                case string:
                    return fromClient.readLine();
            }
        } catch (IOException e) {
            if (!this.clientSocket.isClosed()) {
                logger(statusType.statusUpdate, "The Client has Disconnected"); // Logger
                connectedClients.forEach(player -> {
                    player.playerDelete.put(_deleteSpawnObject);
                });
                mainServerClass.clientList.remove(this);
            }
        }
        return null;
    }
    private void writeClient(Object message) {
        toClient.println(message);
    }
}
