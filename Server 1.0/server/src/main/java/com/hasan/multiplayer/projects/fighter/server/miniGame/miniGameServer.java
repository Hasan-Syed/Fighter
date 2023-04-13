package com.hasan.multiplayer.projects.fighter.server.miniGame;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.hasan.multiplayer.projects.fighter.server.client.mainGameClient;
import com.hasan.multiplayer.projects.fighter.server.enums.logger.serverLogger;
import com.hasan.multiplayer.projects.fighter.server.enums.logger.serverType;

public class miniGameServer extends Thread {
    public final miniGameServer me;
    public final String miniName;
    public final int port;
    public final int maxPlayers;
    public final mainGameClient spawnerEntity;
    public final List<miniGameClient> connectedClient;
    public final List<mainGameClient> allMainClients;
    public boolean playersSatisfied;
    public boolean run;
    public boolean serverLookingForPlayers = false;

    public ServerSocket serverSocket;

    final ExecutorService concurrentRunners; // Executoner Thread Pool

    public miniGameServer(JSONObject gameObject, mainGameClient spawnerEntity, List<mainGameClient> allMainClients)
            throws BindException {
        me = this;
        this.miniName = gameObject.getString("name");
        logger(serverLogger.statusUpdate, "Attempting to start"); // Log Server Spinning Up
        this.port = gameObject.getInt("port");
        this.maxPlayers = gameObject.getInt("maxEntities");
        this.spawnerEntity = spawnerEntity;
        this.connectedClient = new ArrayList<>(maxPlayers);
        this.allMainClients = allMainClients;
        this.concurrentRunners = Executors.newFixedThreadPool(maxPlayers); // Create a Fixed Pool of Clients
        logger(serverLogger.initializePhase, "Looking to Start the miniServer on port: " + port); // Log Server
                                                                                                  // InitialzingPhaze
        try {
            // Initialize and setup serverSocket and attach it to the server port
            serverSocket = new ServerSocket(port);
            logger(serverLogger.initializePhase, "Server Successfully Started on port: " + port); // Log Server
                                                                                                  // InitialzingPhaze
        } catch (Exception e) {
            logger(serverLogger.initializePhase,
                    "The port: " + port + " is occupied, server Launch Failed, return BINDException"); // Log Server
                                                                                                       // InitialzingPhaze
            // The Server port is already Taken throw Exception
            throw new BindException("Port is already Taken");
        }
        run();
    }

    /**
     * {@code getMainClient()} is used to get the main client of the entity
     * 
     * @param clientID
     * @param name
     * @return
     */
    public mainGameClient getMainClient(final int clientID, final String name) {
        return allMainClients.stream()
                .filter((client) -> client.clientID == clientID)
                .toList().get(0);
    }

    /**
     * {@code playerDisconnect()} is used to disconnect players from the mini-game
     * 
     * @param client
     */
    public void playerDisconnect(miniGameClient client) {
        playersSatisfied = false;
        System.out.println(connectedClient.removeIf((clientR) -> clientR.ID == client.ID));
        // create Player Disconnect Object
        JSONObject playerDisconnect = new JSONObject()
                .put("miniName", miniName)
                .put("requestType", "disconnectClient")
                .put("clientInfo", new JSONObject()
                        .put("ID", client.ID))
                .put("playersSatisfied", playersSatisfied);
        // Send to connected Clients
        connectedClient.forEach((connectedClient) -> {
            connectedClient.serverCommand.put(playerDisconnect);
        });
        // Since the server now needs more players to fill the empty spots again run the looking process again
        run();
    }

    public void run() {
        logger(serverLogger.statusUpdate, "Ready for Clients"); // Log Server Status Update
        if (!serverLookingForPlayers){
            serverLookingForPlayers = true;
            new Thread() {
                public void run() {
                    System.out.println(connectedClient.size());
                    while (!playersSatisfied) {
                        try {
                            if (connectedClient.size() == maxPlayers) {
                                playersSatisfied = true;
                            }
                            Socket tempAccept = serverSocket.accept();
                            miniGameClient tempClient = new miniGameClient(tempAccept, me, miniName);
                            connectedClient.add(tempClient);
                            concurrentRunners.execute(tempClient);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    serverLookingForPlayers = false;
                }
            }.start();
        }
    }

    void logger(serverLogger logger, Object message) {
        logger.println(serverType.mini, miniName, message);
    }
}
