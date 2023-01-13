package fr.lernejo.navy_battle;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiGameFire implements HttpHandler {
    private final int port;
    private final String id;
    private final int[][] sea;

    public ApiGameFire(int port, String id){
        this.port = port;
        this.id = id;
        this.sea = createSea();
    }
    public int[][] createSea(){
        int[][] sea = {
            {1,1,1,1,1,0,0,0,0,0},
            {0,0,2,2,2,2,0,0,0,0},
            {3,3,3,0,0,4,4,4,0,0},
            {0,0,0,5,5,0,0,0,0,0}
        };
        return sea;
    }
    public void fire(String cell, HttpExchange exchange, OutputStream os) throws IOException{
        int l = ((int) cell.charAt(0))-'A';
        int c = ((int) cell.charAt(0))-'0';
        if (this.sea[l][c] == 0){
            miss(exchange, os);
        } else if (this.sea[l][c] < 0){
            hit(exchange, os);
        } else {
            this.sea[l][c] *= -1;
            for (int x=0; x < 10; x++) {
                for (int y=0; y < 10; y++){
                    if (this.sea[x][y] == this.sea[l][c]*(-1)){
                        hit(exchange, os);
                        return;
                    }
                }
            }
            sunk(exchange, os);
        }
    }

    public void handle(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            if (exchange.getRequestMethod().equals("GET")){
                String requestQuery = exchange.getRequestURI().getQuery();
                System.out.println(requestQuery);
                if (requestQuery.contains("cell=")){
                    fire(requestQuery.replace("cell=",""), exchange, os);
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
    public void miss(HttpExchange exchange, OutputStream os) throws IOException{
        String body = "{ \"consequence\":\"miss\",\"shipLeft\":\"true\"}";
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
    public void hit(HttpExchange exchange, OutputStream os) throws IOException{
        String body = "{ \"consequence\":\"hit\",\"shipLeft\":\"true\"}";
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
    public void sunk(HttpExchange exchange, OutputStream os) throws IOException{
        for (int x=0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (this.sea[x][y] > 0) {
                    String body = "{ \"consequence\":\"sunk\",\"shipLeft\":\"true\"}";
                    exchange.sendResponseHeaders(200, body.length());
                    os.write(body.getBytes());
                    return;
                }
            }
        }
        String body = "{ \"consequence\":\"sunk\",\"shipLeft\":\"false\"}";
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
}
