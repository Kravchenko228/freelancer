package com.freelancer;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        UserService userService = new UserService(new UserRepository());
        BusinessService businessService = new BusinessService(new BusinessRepository("businesses.ser"));

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is listening on port 8080");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                new ServerThread(socket, userService, businessService).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
