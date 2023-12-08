package com.example.webchat.Services;

import com.example.webchat.Models.User;
import com.example.webchat.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    public Optional<User> getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }
    public Optional<User> getUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
    public User createUser(String email, String username, String password) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Email, username, and password cannot be empty.");
        }
        Optional<User> existingUserWithEmail = userRepository.findUserByEmail(email);
        if (existingUserWithEmail.isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        Optional<User> existingUserWithUsername = userRepository.findUserByUsername(username);
        if (existingUserWithUsername.isPresent()) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        User user = userRepository.insert(new User(email, username, password));
        return user;
    }
}
