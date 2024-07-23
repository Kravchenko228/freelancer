package com.freelancer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import com.freelancer.*;

public class Controller {
    private UserService userService;
    private BusinessService businessService;
    private IAlgoCache<String, User> userCache;
    private IAlgoCache<String, Business> businessCache;

    public Controller(UserService userService, BusinessService businessService, IAlgoCache<String, User> userCache, IAlgoCache<String, Business> businessCache) {
        this.userService = userService;
        this.businessService = businessService;
        this.userCache = userCache;
        this.businessCache = businessCache;
    }

    public void handleRequest(String requestPath, String payload, PrintWriter out) {
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
            } else if (requestPath.equals("/listBusinesses")) {
                handleListBusinesses(out);
            } else {
                handleStaticFiles(requestPath, out);
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
            userCache.put(user.get().getId(), user.get());
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

        businessCache.put(business.getId(), business);
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

            businessCache.put(business.getId(), business);
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
            businessCache.remove(id);
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

    private void handleStaticFiles(String requestPath, PrintWriter out) {
        if (requestPath.contains("?")) {
            requestPath = requestPath.substring(0, requestPath.indexOf('?'));
        }
        String filePath = "src/main/resources/web" + requestPath;
        System.out.println("Serving static file: " + filePath);

        File file = new File(filePath);
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        System.out.println("Absolute file path: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        System.out.println("File is file: " + file.isFile());

        if (file.exists() && file.isFile()) {
            serveFile(file, out);
        } else if (file.exists() && file.isDirectory()) {
            
            File indexFile = new File(file, "index.html");
            if (indexFile.exists() && indexFile.isFile()) {
                serveFile(indexFile, out);
            } else {
                out.print("HTTP/1.1 404 Not Found\r\n\r\n");
                out.flush();
            }
        } else {
            out.print("HTTP/1.1 404 Not Found\r\n\r\n");
            out.flush();
        }
    }

    private void serveFile(File file, PrintWriter out) {
        String contentType = getContentType(file.getPath());
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line;
            out.print("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n");
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Error serving file: " + file.getAbsolutePath());
            e.printStackTrace();
            out.print("HTTP/1.1 500 Internal Server Error\r\n\r\n");
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
}
