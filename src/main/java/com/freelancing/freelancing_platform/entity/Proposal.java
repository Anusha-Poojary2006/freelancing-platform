package com.freelancing.freelancing_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "proposals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    private Double bidAmount;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private Integer deliveryTime;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {

        submittedAt = LocalDateTime.now();

        if (status == null) {
            status = ProposalStatus.PENDING;
        }
    }

    public enum ProposalStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        WITHDRAWN
    }
}