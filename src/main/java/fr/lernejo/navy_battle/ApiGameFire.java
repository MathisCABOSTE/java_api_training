package fr.lernejo.navy_battle;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiGameFire implements HttpHandler {
    private final int port;
    private final String id;
    public ApiGameFire(int port, String id){
        this.port = port;
        this.id = id;
    }
    public void handle(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            if (exchange.getRequestMethod().equals("GET")){
                String requestQuery = exchange.getRequestURI().getQuery();
                System.out.println(requestQuery);
                if (requestQuery.contains("cell=")){
                    gameStart(exchange, os);
                }
                else {badRequest(exchange, os);}
            } else {notFound(exchange, os);}
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }
    public void badRequest(HttpExchange exchange, OutputStream os) throws IOException{
        String body= "400 (Bad Request)";
        exchange.sendResponseHeaders(400,  body.length());
        os.write(body.getBytes());
    }
    public void notFound(HttpExchange exchange, OutputStream os) throws IOException {
        String body = "404 (Not Found)";
        exchange.sendResponseHeaders(404, body.length());
        os.write(body.getBytes());
    }
    public void gameStart(HttpExchange exchange, OutputStream os) throws IOException{
        String body = "{ \"consequence\":\"hit\",\"shipLeft\":\"true\"}";
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
}
