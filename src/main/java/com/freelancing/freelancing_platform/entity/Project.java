package com.freelancing.freelancing_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    private String title;

    private String category;

    private Double budget;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String requiredSkills;

    private LocalDate deadline;

    private String experienceLevel;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = ProjectStatus.OPEN;
        }
    }

    public enum ProjectStatus {
        OPEN,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}