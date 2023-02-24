package com.hasan.multiplayer.projects.fighter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hasan.multiplayer.projects.fighter.server.client.client;

public class server {
    public int maxPlayers = 4;

    public List<client> clientList = new ArrayList<>(maxPlayers);
    ExecutorService concurrentRunners = Executors.newFixedThreadPool(maxPlayers); // Executoner Thread Pool

    int servicePort = 5050;
    
    public void serverSocket () {
        try (ServerSocket socket = new ServerSocket(servicePort)){
            System.out.println("[Server]: Server is now running on: " + servicePort);
            while (true) {
                Socket tempAccept = socket.accept();
                client tempClient = new client(tempAccept, this);

                clientList.add(tempClient);
                concurrentRunners.execute(tempClient);
            }
        } catch (IOException error) {
            System.err.println(error);
        }
    }
}
