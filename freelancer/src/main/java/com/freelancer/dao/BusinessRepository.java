package com.freelancer.dao;

import com.freelancer.Business;

import java.io.*;
import java.util.*;


public class BusinessRepository implements IDAO<Business> {
    private String filePath;
    private Map<String, Business> businesses = new HashMap<>();

    public BusinessRepository(String filePath) {
        this.filePath = filePath;
        loadData();
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            businesses = (Map<String, Business>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            businesses = new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(businesses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Business entity) {
        businesses.put(entity.getId(), entity);
        saveData();
    }

    @Override
    public void update(Business entity) {
        businesses.put(entity.getId(), entity);
        saveData();
    }

    @Override
    public void delete(String id) {
        businesses.remove(id);
        saveData();
    }

    @Override
    public Business get(String id) {
        return businesses.get(id);
    }

    @Override
    public List<Business> getAll() {
        loadData();
        return new ArrayList<>(businesses.values());
    }
}
