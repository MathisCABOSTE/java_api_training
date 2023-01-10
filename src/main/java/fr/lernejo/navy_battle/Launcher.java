package fr.lernejo.navy_battle;

import java.io.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class Launcher {
    public static void main(String[] args) throws java.io.IOException, java.lang.InterruptedException {
        try {
            int port = Integer.parseInt(args[0]);
            String id = UUID.randomUUID().toString();
            var server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("Creating context '/ping' ...");
            server.createContext("/ping", new Ping());
            System.out.println("Creating context '/api/game/start' ...");
            ApiGameStart apiGameStart = new ApiGameStart();
            apiGameStart.init(port, id);
            server.createContext("/api/game/start", apiGameStart);
            System.out.println("Setting executor ...");
            server.setExecutor(null);
            System.out.println("Starting server ...");
            server.start();
            System.out.println("Server started.");
            if (args.length > 1) {
                String target = args[1];
                HttpClient httpClient = HttpClient.newHttpClient();
                String gameStartJson = "{ \"id\":\""+ id + "\",\"url\":\"http://localhost:"+ Integer.toString(port) +"\",\"message\": \"I'm dumb. But let's play anyway :D\"}";
                HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(target + "/api/game/start"))
                    .setHeader("Accept", "application/json")
                    .setHeader("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gameStartJson))
                    .build();
                var response = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
