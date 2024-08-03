package com.freelancer;

import com.freelancer.dao.UserRepository;
import java.util.Optional;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findUser(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public void register(User user) {
        if (userExists(user.getUsername())) {
            throw new RuntimeException("User already exists");
        }
        userRepository.saveUser(user);
    }

    public boolean userExists(String username) {
        return userRepository.findUser(username).isPresent();
    }
}
