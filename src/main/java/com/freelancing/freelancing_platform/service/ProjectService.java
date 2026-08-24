package com.freelancing.freelancing_platform.service;

import com.freelancing.freelancing_platform.dto.ProjectRequest;
import com.freelancing.freelancing_platform.entity.Project;
import com.freelancing.freelancing_platform.entity.User;
import com.freelancing.freelancing_platform.repository.ProjectRepository;
import com.freelancing.freelancing_platform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Create a project
    public Project createProject(ProjectRequest request, String email) {

        User client = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (client.getRole() != User.Role.CLIENT) {
            throw new RuntimeException(
                    "Only clients can create projects"
            );
        }

        Project project = new Project();

        project.setClient(client);
        project.setTitle(request.getTitle());
        project.setCategory(request.getCategory());
        project.setBudget(request.getBudget());
        project.setDescription(request.getDescription());
        project.setRequiredSkills(request.getRequiredSkills());
        project.setDeadline(request.getDeadline());
        project.setExperienceLevel(request.getExperienceLevel());

        return projectRepository.save(project);
    }

    // Get all projects
    public List<Project> getAllProjects() {

        return projectRepository.findAll();
    }

    // Get project by ID
    public Project getProjectById(Long id) {

        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));
    }
    public Project updateProject(Long id, ProjectRequest request, String email) {

    Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    User client = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Only the owner/client can edit the project
    if (project.getClient().getId() == null ||
            !project.getClient().getId().equals(client.getId())) {

        throw new RuntimeException(
                "You can only edit your own project"
        );
    }

    project.setTitle(request.getTitle());
    project.setCategory(request.getCategory());
    project.setBudget(request.getBudget());
    project.setDescription(request.getDescription());
    project.setRequiredSkills(request.getRequiredSkills());
    project.setDeadline(request.getDeadline());
    project.setExperienceLevel(request.getExperienceLevel());

    return projectRepository.save(project);
}
public void deleteProject(Long id, String email) {

    Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    User client = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Only the owner/client can delete the project
    if (project.getClient().getId() == null ||
            !project.getClient().getId().equals(client.getId())) {

        throw new RuntimeException(
                "You can only delete your own project"
        );
    }

    projectRepository.delete(project);
}
public List<Project> searchProjects(String keyword) {

    return projectRepository
            .findByTitleContainingIgnoreCase(keyword);
}
public List<Project> filterByCategory(String category) {

    return projectRepository
            .findByCategoryIgnoreCase(category);
}

}