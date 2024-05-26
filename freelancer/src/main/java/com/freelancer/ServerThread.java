package com.freelancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            StringBuilder request = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                request.append(line).append("\n");
            }

            // Log the received request for debugging
            System.out.println("Received request:\n" + request.toString());

            // Send a basic HTTP response
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                  "Content-Type: text/html\r\n" +
                                  "Content-Length: 19\r\n" +
                                  "\r\n" +
                                  "<h1>Hello World</h1>";
            out.print(httpResponse);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
