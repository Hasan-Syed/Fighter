package com.hasan.multiplayer.projects.fighter.server.miniGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.fighter.server.client.mainGameClient;
import com.hasan.multiplayer.projects.fighter.server.enums.logger.miniGameStatus;
import com.hasan.multiplayer.projects.fighter.server.enums.multiplayer.serverMessageReturnType;

public class miniGameClient extends Thread {
    // Client Settings
    String[] clientReq = { "clientID?", "username" };
    int ID;
    String name;
    // Client
    final String miniName;
    private final BufferedReader fromClient;
    private final PrintWriter toClient;
    private boolean clientSatisfied;
    final miniGameServer miniServer;
    final Socket client;
    mainGameClient mainGameClient;
    // mini-Server sent Command
    public JSONArray serverCommand = new JSONArray();

    /**
     * 
     * @param client
     * @param miniServer
     * @throws IOException
     */
    public miniGameClient(Socket client,
            miniGameServer miniServer, String miniName) throws IOException {
        this.miniName = miniName;
        this.client = client;
        this.miniServer = miniServer;
        this.clientSatisfied = false;
        // Initialize Reader/Writer
        this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.toClient = new PrintWriter(client.getOutputStream(), true);
        // Initialize Cliant
        initializeClient();
    }

    /**
     * {@code initializeClient} is used to initialize clients to the game
     */
    void initializeClient() {
        logger(miniGameStatus.statusUpdate, "Initializing Server from Client");
        writeClient(new JSONObject().put("initialize-server", true));
        // Initialize Server
        for (String request : clientReq) {
            // Request Requirement
            writeClient(new JSONObject().put("request", request));
            // get Client Reply
            JSONObject reply = (JSONObject) readClient(serverMessageReturnType.jsonObject);
            logger(miniGameStatus.requested, new JSONObject().put("request", request)); // Logger
            // Check and Apply
            if (reply.has(request)) {
                logger(miniGameStatus.response, "client responded -> " + reply); // Log
                switch (request) {
                    case "username" -> {
                        this.name = reply.getString(request);
                        this.setName("miniGameClient=" + name);
                    }
                    case "clientID?" -> {
                        this.ID = reply.getInt(request);
                    }
                }
            }
        }
        // finish server Initialze
        writeClient(new JSONObject().put("request", "initialize-Client"));
        logger(miniGameStatus.statusUpdate, "switched to client Initialzation"); // Logger
        boolean initializeClient = true;
        // Start Client Initilize
        while (initializeClient) {
            // Get Requested Item
            JSONObject requested = (JSONObject) readClient(serverMessageReturnType.jsonObject);
            logger(miniGameStatus.requested, requested); // Logger
            String requestedItem = requested.getString("request");
            // process Request
            switch (requestedItem) {
                case "connectedEntities?" -> {
                    JSONArray clientList = new JSONArray();
                    miniServer.connectedClient.forEach(client -> clientList.put(client.ID));
                    writeClient(new JSONObject().put("connectedEntities?", clientList));
                    logger(miniGameStatus.response, new JSONObject().put("connectedEntities?", clientList)); // Logger
                }
                case "finish-Initialize" -> {
                    logger(miniGameStatus.requested, "finish Initalize"); // Logger
                    initializeClient = false;
                    break; // break out of loop
                }
            }
        }
        logger(miniGameStatus.statusUpdate, "Initialize Finish"); // Logger
        // get main Client
        mainGameClient = miniServer.getMainClient(ID, name);
        this.start();
    }

    private JSONArray returnObject;
    
    public synchronized void run() {
        boolean run= true;
        while (run) {
            returnObject = new JSONArray();
            // Getting Client
            JSONObject clientInput = (JSONObject) readClient(serverMessageReturnType.jsonObject);
            // This is for Player Satisfaction on all connected Clients
            {
                // Reading Player Satisfation
                if (clientInput.has("lobbySatisfy") && !clientInput.getBoolean("lobbySatisfy")
                && miniServer.playersSatisfied) {
                    // Create a clientList
                    JSONArray clientList = new JSONArray();
                    // fill client List
                    miniServer.connectedClient.forEach(client -> clientList.put(client.ID));
                    // Create return object
                    JSONObject playerSatisfy = new JSONObject()
                    .put("miniName", miniName)
                    .put("requestType", "satisfy")
                    .put("connectedEntities", clientList)
                    .put("playersSatisfied", miniServer.playersSatisfied);
                    // place new client Object on the return array
                    returnObject.put(playerSatisfy);
                }
            }
            // Transfer extras
            {
                if (clientInput.has("gameData")){
                    JSONObject whatAmI = clientInput.getJSONObject("gameData");
                    miniServer.connectedClient.forEach(client -> {if (client.ID != ID){client.serverCommand.put(whatAmI);}});
                }
            }
            // Reading if the Player wants to disconnect from the client
            {
                // checking of the disconnect is requested
                if (clientInput.has("disconnectMe") && clientInput.getBoolean("disconnectMe")){
                    // Tell all connected clients about the disconnect
                    miniServer.playerDisconnect(this);
                    // break and close this miniGameClient
                    run = false;
                    break;
                }
            }
            // Check if any Server commands are in list to go
            {
                // Checking if the serverCommand array is not empty
                if (!serverCommand.isEmpty()){
                    // take the server commands and place them on the return array
                    returnObject.putAll(serverCommand);
                    serverCommand.clear();
                }
            }
            // Send data to client
            writeClient(returnObject);
        }
    }

    /**
     * {@code readClient()} is used to send messages/requests to the client
     * 
     * @param returnType messages/requests
     * @return
     */
    public Object readClient(serverMessageReturnType returnType) {
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

        }
        return null;
    }

    /**
     * {@code writeClient()} is used send messeges/requests to the client
     * 
     * @param message
     */
    public void writeClient(Object message) {
        toClient.println(message);
    }

    public void logger(miniGameStatus status, Object message) {
        status.println(ID, miniName, message);
    }
}
