package fr.lernejo.navy_battle;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiGameStart implements HttpHandler {
    int port;
    String id;
    public void init(int port, String id){
        this.port = port;
        this.id = id;
    }
    public void handle(HttpExchange exchange) throws IOException {
        String body = "{ \"id\":\""+ this.id + "\",\"url\":\"http://localhost:"+ Integer.toString(this.port) +"\",\"message\": \"Get ready for the battle !\"}";
        try (OutputStream os = exchange.getResponseBody()) {
            if (exchange.getRequestMethod().equals("POST")){
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(requestBody);
                if (requestBody.contains("id") && requestBody.contains("url") && requestBody.contains("message")){
                    exchange.sendResponseHeaders(200, body.length());
                    os.write(body.getBytes());
                }

            } else {
                body = "404 (Not Found)";
                exchange.sendResponseHeaders(404,body.length());
                os.write(body.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
