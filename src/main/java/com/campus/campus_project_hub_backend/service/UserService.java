package com.campus.campus_project_hub_backend.service;

import org.springframework.stereotype.Service;

import com.campus.campus_project_hub_backend.entity.User;
import com.campus.campus_project_hub_backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {

        if (user.getEmail() == null || user.getPassword() == null) {
            return null;
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return null;
        }

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {

        if (email == null || password == null) {
            return null;
        }

        User user = userRepository.findByEmail(email);

        if (user != null
                && user.getPassword() != null
                && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setBranch(updatedUser.getBranch());
        existingUser.setYear(updatedUser.getYear());
        existingUser.setSkills(updatedUser.getSkills());

        return userRepository.save(existingUser);
    }
}
