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
        int c = ((int) cell.charAt(1))-'1';
        if (l > 0 || l > 10 || c < 0 || c > 10) {
            badRequest(exchange, os);
        }
        System.out.println(Integer.toString(l) + " " + Integer.toString(c));
        if (this.sea[l][c] == 0){
            miss(exchange, os);
        } else if (this.sea[l][c] < 0){
            hit(exchange, os);
        } else {
            this.sea[l][c] *= -1;
            if (isSunk(this.sea[l][c])){
                sunk(exchange, os);
            } else {
                hit(exchange, os);
            }
        }
    }
    public boolean isSunk(int target) {
        for (int x=0; x < 10; x++) {
            for (int y=0; y < 10; y++){
                if (this.sea[x][y] == target*(-1)){
                    return true;
                }
            }
        }
        return false;
    }
    public void handle(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            String requestQuery = exchange.getRequestURI().getQuery();
            if (requestQuery.contains("cell=")){
                fire(requestQuery.replace("cell=",""), exchange, os);
            } else {
                badRequest(exchange, os);
            }
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
        System.out.println(body);
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
    public void hit(HttpExchange exchange, OutputStream os) throws IOException{
        String body = "{ \"consequence\":\"hit\",\"shipLeft\":\"true\"}";
        System.out.println(body);
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
    public void sunk(HttpExchange exchange, OutputStream os) throws IOException{
        for (int x=0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (this.sea[x][y] > 0) {
                    String body = "{ \"consequence\":\"sunk\",\"shipLeft\":\"true\"}";
                    System.out.println(body);
                    exchange.sendResponseHeaders(200, body.length());
                    os.write(body.getBytes());
                    return;
                }
            }
        }
        String body = "{ \"consequence\":\"sunk\",\"shipLeft\":\"false\"}";
        System.out.println(body);
        exchange.sendResponseHeaders(200, body.length());
        os.write(body.getBytes());
    }
}
