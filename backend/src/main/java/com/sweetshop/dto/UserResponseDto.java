package com.sweetshop.dto;

import java.util.Set;

public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles; // A set of role names is sufficient

    public UserResponseDto() {}

    public UserResponseDto(Long id, String email, String firstName, String lastName, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}