package fr.lernejo.navy_battle;

import java.io.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Launcher {
    public static void main(String[] args) throws java.io.IOException {
        try {
            var server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
            server.createContext("/ping", new HttpRequestHandler());
            server.createContext("/api/game/start", new ApiGameStart());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}