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
    public static void createContexts(int port, String id) throws  IOException{
        var server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Creating contexts");
        server.createContext("/ping", new Ping());
        ApiGameStart apiGameStart = new ApiGameStart(port,id);
        server.createContext("/api/game/start", apiGameStart);
        ApiGameFire apiGameFire = new ApiGameFire(port,id);
        server.createContext("/api/game/fire", apiGameFire);
        System.out.println("Setting executor ...");
        server.setExecutor(null);
        System.out.println("Starting server ...");
        server.start();
        System.out.println("Server started.");
    }
    public static HttpResponse<String> sendGameStartJson(HttpClient httpClient, int port, String id, String target) throws InterruptedException, IOException{
        String gameStartJson = "{ \"id\":\""+ id + "\",\"url\":\"http://localhost:"+ Integer.toString(port) +"\",\"message\": \"I'm dumb. But let's play anyway :D\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(URI.create(target + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(gameStartJson))
            .build();
        return httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
    }
    public static void main(String[] args) throws InterruptedException{
        try {
            int port = Integer.parseInt(args[0]);
            String id = UUID.randomUUID().toString();
            createContexts(port, id);
            if (args.length > 1) {
                String target = args[1];
                HttpClient httpClient = HttpClient.newHttpClient();
                var response = sendGameStartJson(httpClient,port,id,target);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
