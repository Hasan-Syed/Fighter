package com.hasan.multiplayer.projects.fighter.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.fighter.server.client.mainGameClient;
import com.hasan.multiplayer.projects.fighter.server.enums.logger.serverLogger;
import com.hasan.multiplayer.projects.fighter.server.enums.logger.serverType;
import com.hasan.multiplayer.projects.fighter.server.miniGame.miniGameServer;

/**
 * {@code server} is used to create a new server
 * 
 * @author Hasan Syed
 * @version 2.0
 */
public class server {
    // Useful Variables
    boolean playerListFull;
    boolean start;
    final server me;
    // Server Identification Settings
    public final String serverName;
    public final int maxPlayers;
    public final int port;
    // Server - Client Settings
    public final List<mainGameClient> clientList;
    public final List<miniGameServer> miniGameServers;
    // Server - Client Runner
    final ExecutorService concurrentRunners; // Executoner Thread Pool
    final ServerSocket socket;
    Thread serverThread;

    /**
     * {@code server(JSONObject serverObject)} is used to create a new server
     * 
     * @param serverObject
     */
    public server(JSONObject serverObject) throws BindException, IOException, JSONException {
        this.me = this;
        logger(serverLogger.initializePhase, "Initializing");
        // Try to Extract Server Data from server Object
        try {
            this.serverName = serverObject.getString("serverName");
            logger(serverLogger.initializePhase, "serverName=" + serverName);
            this.maxPlayers = serverObject.getInt("maxPlayers");
            logger(serverLogger.initializePhase, "maxPlayer=" + maxPlayers);
            this.port = serverObject.getInt("port");
            logger(serverLogger.initializePhase, "port=" + port);
        } catch (JSONException JE) {
            logger(serverLogger.initializePhase, "proper JSON wasn't forworded, server launch Failed");
            throw JE;
        }
        // Initialize Essentials
        clientList = new ArrayList<>(maxPlayers); // Initialize the Client List
        miniGameServers = new ArrayList<>();
        concurrentRunners = Executors.newFixedThreadPool(maxPlayers); // Create a Fixed Pool of Clients
        logger(serverLogger.initializePhase, "initialized clientList & concurrentRunners");
        // Initialize Server Socket
        logger(serverLogger.initializePhase, "attempting to iniitalize Server Socket on port:" + port);
        try {
            socket = new ServerSocket(port);
            logger(serverLogger.initializePhase, "Server Started on port: " + port);
        } catch (BindException BE) {
            logger(serverLogger.initializePhase, "Port Bound Exception, the Port is already Taken");
            throw BE;
        } catch (IOException IOE) {
            logger(serverLogger.initializePhase, "IO Exception");
            throw IOE;
        }
        // Start the server
        serverSocket();
    }
    /**
     * server socket runs the server
     */
    void serverSocket() {
        logger(serverLogger.statusUpdate, "Server: " + serverName + ", is now ready for clients");
        serverThread = new Thread(serverName) {
            public void run () {
                if (start){
                    // While the Player List is not fill Keep Looping
                    while (!playerListFull) {
                        // Try to Accept the Socket and Spin up a client
                        try {
                            Socket temp = socket.accept();
                            // Accepted Client
                            mainGameClient acceptedClient = new mainGameClient(temp, me);
                            // Add CLient to the Client List
                            clientList.add(acceptedClient);
                            // Spin up the Client
                            concurrentRunners.execute(acceptedClient);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        start = true;
        serverThread.start();   // Start server thread
    }

    void logger(serverLogger logger, Object message) {
        logger.println(serverType.main, serverName, message);
    }
}
