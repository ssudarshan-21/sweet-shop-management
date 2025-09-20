package com.sweetshop.controller;

import com.sweetshop.service.UserService;
import com.sweetshop.entity.User;
import com.sweetshop.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile(@AuthenticationPrincipal User user) {
        User currentUser = userService.findById(user.getId());
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", currentUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok(ApiResponse.success("User disabled successfully", "User disabled"));
    }

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return ResponseEntity.ok(ApiResponse.success("User enabled successfully", "User enabled"));
    }
}