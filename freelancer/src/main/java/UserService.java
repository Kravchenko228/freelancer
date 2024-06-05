package com.freelancer;

import java.util.*;

public class UserService {
    private IDAO<User> userDAO;

    public UserService(IDAO<User> userDAO) {
        this.userDAO = userDAO;
    }

    public void register(User user) {
        userDAO.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userDAO.getAll().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }
}
