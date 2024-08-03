package com.freelancer;

public class ServerDriver {
    public static void main(String[] args) {
        Server server = new Server("users.dat", "businesses.dat");
        new Thread(server).start();
    }
}
