package com.freelancing.freelancing_platform.repository;

import com.freelancing.freelancing_platform.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(Project.ProjectStatus status);

    List<Project> findByCategoryIgnoreCase(String category);

    List<Project> findByTitleContainingIgnoreCase(String title);

    List<Project> findByDescriptionContainingIgnoreCase(String keyword);

    List<Project> findByRequiredSkillsContainingIgnoreCase(String skill);

    List<Project> findByClientId(Long clientId);
}