package com.freelancer;

import java.io.*;
import java.util.*;

public class UserRepository implements IDAO<User> {
    private String filePath;
    private Map<String, User> users = new HashMap<>();

    public UserRepository(String filePath) {
        this.filePath = filePath;
        loadData();
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            users = new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User entity) {
        users.put(entity.getId(), entity);
        saveData();
    }

    @Override
    public void update(User entity) {
        users.put(entity.getId(), entity);
        saveData();
    }

    @Override
    public void delete(String id) {
        users.remove(id);
        saveData();
    }

    @Override
    public User get(String id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
