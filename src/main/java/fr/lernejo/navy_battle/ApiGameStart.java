package fr.lernejo.navy_battle;

import java.io.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiGameStart implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String body = exchange.getRequestBody().toString();
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
