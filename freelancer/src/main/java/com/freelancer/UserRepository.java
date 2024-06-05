package com.freelancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IDAO<User> {
    private List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public void update(User user) {
        User existingUser = get(user.getId());
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
        }
    }

    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    @Override
    public User get(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> find(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }
}
