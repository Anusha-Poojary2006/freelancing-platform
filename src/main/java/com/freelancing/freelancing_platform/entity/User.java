package com.freelancing.freelancing_platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@NotBlank(message = "Name is required")
private String name;


    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
@Email(message = "Enter a valid email")
private String email;

  

    @JsonIgnore
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
@Size(min = 6, message = "Password must contain at least 6 characters")
private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    private String bio;

    private String skills;

    private LocalDateTime createdAt;

    



    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Role {
        CLIENT,
        FREELANCER
    }
}