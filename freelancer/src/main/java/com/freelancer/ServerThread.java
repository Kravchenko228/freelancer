package com.freelancer;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    private void handleStaticFiles(String requestPath, PrintWriter out) {
        if (requestPath.contains("?")) {
            requestPath = requestPath.substring(0, requestPath.indexOf('?'));
        }

        String filePath = "/workspaces/freelancer/freelancer/src/main/java/web" + requestPath;
        System.out.println("Serving static file: " + filePath);

        String contentType = getContentType(filePath);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            out.print("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n");
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("File not found: " + filePath);
            out.print("HTTP/1.1 404 Not Found\r\n\r\n");
            out.flush();
        }
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "text/plain";
        }
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            StringBuilder request = new StringBuilder();
            int contentLength = 0;

            // Read the request headers
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
            if (requestMethod.equals("POST") && contentLength > 0) {
                char[] payload = new char[contentLength];
                in.read(payload, 0, contentLength);
                handlePostRequest(requestPath, new String(payload), out);
            } else {
                if (requestMethod.equals("GET") && requestPath.equals("/")) {
                    handleStaticFiles("/index.html", out);
                } else if (requestMethod.equals("GET") && requestPath.equals("/listBusinesses")) {
                    handleListBusinesses(out);
                } else if (requestMethod.equals("GET") && requestPath.startsWith("/")) {
                    handleStaticFiles(requestPath, out);
                } else {
                    out.print("HTTP/1.1 404 Not Found\r\n\r\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostRequest(String requestPath, String payload, PrintWriter out) {
        try {
            if (requestPath.equals("/register")) {
                handleRegister(payload, out);
            } else if (requestPath.equals("/login")) {
                handleLogin(payload, out);
            } else if (requestPath.equals("/createBusiness")) {
                handleCreateBusiness(payload, out);
            } else if (requestPath.equals("/updateBusiness")) {
                handleUpdateBusiness(payload, out);
            } else if (requestPath.equals("/deleteBusiness")) {
                handleDeleteBusiness(payload, out);
            } else {
                out.print("HTTP/1.1 404 Not Found\r\n\r\n");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            out.print("HTTP/1.1 500 Internal Server Error\r\n\r\n");
            out.flush();
        }
    }

    private void handleRegister(String payload, PrintWriter out) throws IOException {
        String[] params = payload.split("&");
        String username = URLDecoder.decode(params[0].split("=")[1], StandardCharsets.UTF_8.name());
        String password = URLDecoder.decode(params[1].split("=")[1], StandardCharsets.UTF_8.name());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(password);
        userService.register(user);

        out.print("HTTP/1.1 200 OK\r\n\r\n");
        out.flush();
    }

    private void handleLogin(String payload, PrintWriter out) throws IOException {
        String[] params = payload.split("&");
        String username = URLDecoder.decode(params[0].split("=")[1], StandardCharsets.UTF_8.name());
        String password = URLDecoder.decode(params[1].split("=")[1], StandardCharsets.UTF_8.name());

        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            out.print("HTTP/1.1 200 OK\r\n\r\n");
        } else {
            out.print("HTTP/1.1 401 Unauthorized\r\n\r\n");
        }
        out.flush();
    }

    private void handleCreateBusiness(String payload, PrintWriter out) throws IOException {
        String[] params = payload.split("&");
        String name = URLDecoder.decode(params[0].split("=")[1], StandardCharsets.UTF_8.name());
        String description = URLDecoder.decode(params[1].split("=")[1], StandardCharsets.UTF_8.name());
        String contactInfo = URLDecoder.decode(params[2].split("=")[1], StandardCharsets.UTF_8.name());

        Business business = new Business();
        business.setId(UUID.randomUUID().toString());
        business.setName(name);
        business.setDescription(description);
        business.setContactInfo(contactInfo);
        businessService.createBusiness(business);

        out.print("HTTP/1.1 200 OK\r\n\r\n");
        out.flush();
    }

    private void handleUpdateBusiness(String payload, PrintWriter out) throws IOException {
        String[] params = payload.split("&");
        String id = URLDecoder.decode(params[0].split("=")[1], StandardCharsets.UTF_8.name());
        String name = URLDecoder.decode(params[1].split("=")[1], StandardCharsets.UTF_8.name());
        String description = URLDecoder.decode(params[2].split("=")[1], StandardCharsets.UTF_8.name());
        String contactInfo = URLDecoder.decode(params[3].split("=")[1], StandardCharsets.UTF_8.name());

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

    private void handleDeleteBusiness(String payload, PrintWriter out) throws IOException {
        String[] params = payload.split("&");
        String id = URLDecoder.decode(params[0].split("=")[1], StandardCharsets.UTF_8.name());

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
