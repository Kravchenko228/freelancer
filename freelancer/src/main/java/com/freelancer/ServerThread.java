package com.freelancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.freelancer.controller.Controller;

public class ServerThread extends Thread {
    private Socket socket;
    private Controller controller;

    public ServerThread(Socket socket, UserService userService, BusinessService businessService, IAlgoCache<String, User> userCache, IAlgoCache<String, Business> businessCache) {
        this.socket = socket;
        this.controller = new Controller(userService, businessService, userCache, businessCache);
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            StringBuilder request = new StringBuilder();
            int contentLength = 0;

            while ((line = in.readLine()) != null && !line.isEmpty()) {
                request.append(line).append("\n");
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                }
            }

            System.out.println("Received request: " + request.toString());

            String[] requestLines = request.toString().split("\n");
            String requestMethod = requestLines[0].split(" ")[0];
            String requestPath = requestLines[0].split(" ")[1];

            // Read the request payload if it's a POST request
            String payload = "";
            if (requestMethod.equals("POST") && contentLength > 0) {
                char[] buffer = new char[contentLength];
                in.read(buffer);
                payload = new String(buffer);
            }

            controller.handleRequest(requestPath, payload, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
