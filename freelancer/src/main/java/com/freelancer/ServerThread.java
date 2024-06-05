package com.freelancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;

public class ServerThread extends Thread {
    private Socket socket;
    private UserService userService;
    private BusinessService businessService;

    public ServerThread(Socket socket, UserService userService, BusinessService businessService) {
        this.socket = socket;
        this.userService = userService;
        this.businessService = businessService;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            StringBuilder request = new StringBuilder();
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                request.append(line).append("\n");
            }

            System.out.println("Received request: " + request.toString());

            String[] requestLines = request.toString().split("\n");
            String requestMethod = requestLines[0].split(" ")[0];
            String requestPath = requestLines[0].split(" ")[1];

            if (requestMethod.equals("GET") && requestPath.equals("/")) {
                out.print("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n");
                out.print("<html><body><h1>Welcome to Freelancer</h1></body></html>");
                out.flush();
            } else if (requestMethod.equals("POST") && requestPath.equals("/register")) {
                handleRegister(in, out);
            } else if (requestMethod.equals("POST") && requestPath.equals("/login")) {
                handleLogin(in, out);
            } else if (requestMethod.equals("POST") && requestPath.equals("/createBusiness")) {
                handleCreateBusiness(in, out);
            } else if (requestMethod.equals("POST") && requestPath.equals("/updateBusiness")) {
                handleUpdateBusiness(in, out);
            } else if (requestMethod.equals("POST") && requestPath.equals("/deleteBusiness")) {
                handleDeleteBusiness(in, out);
            } else if (requestMethod.equals("GET") && requestPath.equals("/listBusinesses")) {
                handleListBusinesses(out);
            } else {
                out.print("HTTP/1.1 404 Not Found\r\n\r\n");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRegister(BufferedReader in, PrintWriter out) throws IOException {
        StringBuilder payload = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            payload.append(line);
        }

        String[] params = payload.toString().split("&");
        String username = params[0].split("=")[1];
        String password = params[1].split("=")[1];

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(password);
        userService.register(user);

        out.print("HTTP/1.1 200 OK\r\n\r\n");
        out.flush();
    }

    private void handleLogin(BufferedReader in, PrintWriter out) throws IOException {
        StringBuilder payload = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            payload.append(line);
        }

        String[] params = payload.toString().split("&");
        String username = params[0].split("=")[1];
        String password = params[1].split("=")[1];

        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            out.print("HTTP/1.1 200 OK\r\n\r\n");
        } else {
            out.print("HTTP/1.1 401 Unauthorized\r\n\r\n");
        }
        out.flush();
    }

    private void handleCreateBusiness(BufferedReader in, PrintWriter out) throws IOException {
        StringBuilder payload = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            payload.append(line);
        }

        String[] params = payload.toString().split("&");
        String name = params[0].split("=")[1];
        String description = params[1].split("=")[1];
        String contactInfo = params[2].split("=")[1];

        Business business = new Business();
        business.setId(UUID.randomUUID().toString());
        business.setName(name);
        business.setDescription(description);
        business.setContactInfo(contactInfo);
        businessService.createBusiness(business);

        out.print("HTTP/1.1 200 OK\r\n\r\n");
        out.flush();
    }

    private void handleUpdateBusiness(BufferedReader in, PrintWriter out) throws IOException {
        StringBuilder payload = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            payload.append(line);
        }

        String[] params = payload.toString().split("&");
        String id = params[0].split("=")[1];
        String name = params[1].split("=")[1];
        String description = params[2].split("=")[1];
        String contactInfo = params[3].split("=")[1];

        Business business = businessService.getBusiness(id);
        if (business != null) {
            business.setName(name);
            business.setDescription(description);
            business.setContactInfo(contactInfo);
            businessService.updateBusiness(business);
            out.print("HTTP/1.1 200 OK\r\n\r\n");
        } else {
            out.print("HTTP/1.1 404 Not Found\r\n\r\n");
        }
        out.flush();
    }

    private void handleDeleteBusiness(BufferedReader in, PrintWriter out) throws IOException {
        StringBuilder payload = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            payload.append(line);
        }

        String id = payload.toString().split("=")[1];

        Business business = businessService.getBusiness(id);
        if (business != null) {
            businessService.deleteBusiness(id);
            out.print("HTTP/1.1 200 OK\r\n\r\n");
        } else {
            out.print("HTTP/1.1 404 Not Found\r\n\r\n");
        }
        out.flush();
    }

    private void handleListBusinesses(PrintWriter out) {
        out.print("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n");
        out.print("[");
        boolean first = true;
        for (Business business : businessService.getAllBusinesses()) {
            if (!first) {
                out.print(",");
            }
            out.print("{\"id\":\"" + business.getId() + "\",\"name\":\"" + business.getName() + "\",\"description\":\"" + business.getDescription() + "\",\"contactInfo\":\"" + business.getContactInfo() + "\"}");
            first = false;
        }
        out.print("]");
        out.flush();
    }
}
