package com.freelancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8080;
    private UserService userService;
    private BusinessService businessService;

    public Server(String userFilePath, String businessFilePath) {
        UserRepository userRepository = new UserRepository(userFilePath);
        BusinessRepository businessRepository = new BusinessRepository(businessFilePath);
        this.userService = new UserService(userRepository);
        this.businessService = new BusinessService(businessRepository);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerThread(socket, userService, businessService).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server("users.dat", "businesses.dat");
        server.start();
    }
}
