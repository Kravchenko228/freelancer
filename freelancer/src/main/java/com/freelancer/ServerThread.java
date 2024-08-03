package com.freelancer;

import com.freelancer.controller.Controller;
import com.freelancer.dao.BusinessRepository;
import com.freelancer.dao.UserRepository;

import java.io.*;
import java.net.Socket;
import java.io.IOException;


public class ServerThread extends Thread {
    private Socket socket;
    private UserService userService;
    private BusinessService businessService;
    private IAlgoCache<String, User> userCache;
    private IAlgoCache<String, Business> businessCache;

    public ServerThread(Socket socket, UserService userService, BusinessService businessService,
                        IAlgoCache<String, User> userCache, IAlgoCache<String, Business> businessCache) {
        this.socket = socket;
        this.userService = userService;
        this.businessService = businessService;
        this.userCache = userCache;
        this.businessCache = businessCache;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String request = in.readLine();
            String data = in.readLine();

            UserService userService = new UserService(new UserRepository("users.dat"));
            BusinessService businessService = new BusinessService(new BusinessRepository("businesses.dat"));
            IAlgoCache<String, User> userCache = new SimpleCache<>();
            IAlgoCache<String, Business> businessCache = new SimpleCache<>();

            Controller controller = new Controller(userService, businessService, userCache, businessCache);

            controller.handleRequest(request, data, out);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
