package com.hasan.multiplayer.projects.flighter.game.multiplayer.personalLobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.enums.logger.statusType;
import com.hasan.multiplayer.projects.flighter.game.enums.multiplayer.serverMessageReturnType;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

public abstract class miniGameLobby {
    public final int ID;
    public final int port;
    public final gamePanel gp;
    public final gameObject application;
    protected final Socket entitySocket;
    protected final BufferedReader fromServer;
    protected final PrintWriter toServer;

    public miniGameLobby(gamePanel gp, superObject application, int port) throws IOException {
        this.gp = gp;
        this.port = port;
        // Initialize Lobby Connected Entity
        logger(statusType.statusUpdate,
                "Initializing " + application.name + " lobby connection, spawned by " + application.spawnedByE.name);
        this.ID = application.ID;
        this.application = application;
        try {
            this.entitySocket = new Socket(gp.connectionIP, port);
            gp.hud.addNewMidNotification(port, "connection to mini EstB", 3, "[lobbyConnectedEntity]",
                    notifyType.success);
        } catch (IOException IOE) {
            throw IOE;
        }
        // Server Talks
        try {
            this.fromServer = new BufferedReader(new InputStreamReader(entitySocket.getInputStream()));
            this.toServer = new PrintWriter(entitySocket.getOutputStream(), true);
        } catch (IOException IOE) {
            throw IOE;
        }
    }

    /**
     * {@code initialize()} is used to for the initial conversation between the
     * server and the client
     */
    public abstract void initialize();

    /**
     * {@code readServer} is used to read messages/requests from the server
     * 
     * @param returnType type of object expected
     * @return
     */
    public Object readServer(serverMessageReturnType returnType) {
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

    /**
     * {@code writeServer} is used to send messages/requests to the server
     * 
     * @param message messages/requests
     */
    public void writeServer(Object message) {
        toServer.println(message);
    }

    public void closeConnection () {
        try {
            entitySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected (){
        return entitySocket.isConnected();
    }
    /**
     * logger, used to log events in console
     * 
     * @param statType statusType
     * @param message  message to log
     */
    public void logger(statusType statType, Object message) {
        statType.println(gp.connectionIP + ":" + port, message);
    }
}
