package com.freelancing.freelancing_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be greater than zero")
    private Double budget;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Required skills are required")
    private String requiredSkills;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    @NotBlank(message = "Experience level is required")
    private String experienceLevel;
}