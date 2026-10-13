package com.campus.campus_project_hub_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_project_hub_backend.entity.User;
import com.campus.campus_project_hub_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://campus-project-hub-frontend.onrender.com"
})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);

        if (registeredUser == null) {
            return ResponseEntity.badRequest()
                    .body("Registration failed. Email may already exist or required fields are missing.");
        }

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User loggedInUser = userService.loginUser(
                user.getEmail(),
                user.getPassword()
        );

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body("Invalid email or password.");
        }

        return ResponseEntity.ok(loggedInUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {

        User updatedUser = userService.updateUser(id, user);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser);
    }
}