package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.License;
import com.itm.ms365managementservice.entities.User;
import com.itm.ms365managementservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User updatedUser) {
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<License> getLicensesByUserId(String userId) {
        return userRepository.findById(userId)
                .map(User::getLicenses)
                .orElse(Collections.emptyList());
    }

    public User getUserByUserPrincipalName(String username) {
        return userRepository.findByUserPrincipalName(username)
                .orElse(null);
    }
}
