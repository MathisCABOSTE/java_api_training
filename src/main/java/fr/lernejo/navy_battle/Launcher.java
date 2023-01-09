package fr.lernejo.navy_battle;

import java.io.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Launcher {
    public static void main(String[] args) throws java.io.IOException {
        try {
            var server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
            System.out.println("Creating context '/ping' ...");
            server.createContext("/ping", new HttpRequestHandler());
            System.out.println("Creating context '/api/game/start' ...");
            server.createContext("/api/game/start", new ApiGameStart());
            System.out.println("Setting executor ...");
            server.setExecutor(null);
            System.out.println("Starting server ...");
            server.start();
            System.out.println("Server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
