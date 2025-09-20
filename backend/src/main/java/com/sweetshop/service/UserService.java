package com.sweetshop.service;

import com.sweetshop.entity.User;
import com.sweetshop.entity.Role;
import com.sweetshop.repository.UserRepository;
import com.sweetshop.repository.RoleRepository;
import com.sweetshop.dto.UserRegistrationDto;
import com.sweetshop.exception.UserAlreadyExistsException;
import com.sweetshop.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registrationDto.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEnabled(true);

        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        user.addRole(userRole);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> findUsersByRole(String roleName) {
        return userRepository.findByRolesName(roleName);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void disableUser(Long userId) {
        User user = findById(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void enableUser(Long userId) {
        User user = findById(userId);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}
