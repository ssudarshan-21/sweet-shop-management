package com.sweetshop.service;

import com.sweetshop.entity.User;
import com.sweetshop.entity.Role;
import com.sweetshop.repository.UserRepository;
import com.sweetshop.repository.RoleRepository;
import com.sweetshop.dto.UserRegistrationDto;
import com.sweetshop.exception.UserAlreadyExistsException;
import com.sweetshop.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void shouldRegisterNewUser() {
        // Given
        UserRegistrationDto registrationDto = new UserRegistrationDto(
            "test@example.com", "password123", "John", "Doe"
        );
        
        Role userRole = new Role("USER", "Regular user");
        
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User result = userService.registerUser(registrationDto);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encoded-password", result.getPassword());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertTrue(result.getRoles().contains(userRole));
        
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        UserRegistrationDto registrationDto = new UserRegistrationDto(
            "existing@example.com", "password123", "John", "Doe"
        );
        
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
        
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldFindUserByEmail() {
        // Given
        String email = "test@example.com";
        User expectedUser = new User(email, "password", "John", "Doe");
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // When
        User result = userService.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.findByEmail(email);
        });
        
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldValidatePassword() {
        // Given
        User user = new User("test@example.com", "encoded-password", "John", "Doe");
        String rawPassword = "password123";
        
        when(passwordEncoder.matches(rawPassword, "encoded-password")).thenReturn(true);

        // When
        boolean isValid = userService.validatePassword(user, rawPassword);

        // Then
        assertTrue(isValid);
        verify(passwordEncoder).matches(rawPassword, "encoded-password");
    }

    @Test
    void shouldReturnFalseForInvalidPassword() {
        // Given
        User user = new User("test@example.com", "encoded-password", "John", "Doe");
        String wrongPassword = "wrongpassword";
        
        when(passwordEncoder.matches(wrongPassword, "encoded-password")).thenReturn(false);

        // When
        boolean isValid = userService.validatePassword(user, wrongPassword);

        // Then
        assertFalse(isValid);
        verify(passwordEncoder).matches(wrongPassword, "encoded-password");
    }
}