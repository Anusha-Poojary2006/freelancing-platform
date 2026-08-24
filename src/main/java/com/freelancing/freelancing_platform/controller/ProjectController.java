package com.freelancing.freelancing_platform.controller;

import com.freelancing.freelancing_platform.dto.ProjectRequest;
import com.freelancing.freelancing_platform.entity.Project;
import com.freelancing.freelancing_platform.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestBody ProjectRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Project project =
                projectService.createProject(request, email);

        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {

        return ResponseEntity.ok(
                projectService.getAllProjects()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                projectService.getProjectById(id)
        );
    }
    @PutMapping("/{id}")
public ResponseEntity<Project> updateProject(
        @PathVariable Long id,
        @RequestBody @Valid ProjectRequest request,
        Authentication authentication) {

    String email = authentication.getName();

    Project project =
            projectService.updateProject(id, request, email);

    return ResponseEntity.ok(project);
}
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteProject(
        @PathVariable Long id,
        Authentication authentication) {

    String email = authentication.getName();

    projectService.deleteProject(id, email);

    return ResponseEntity.ok("Project deleted successfully");
}
@GetMapping("/search")
public ResponseEntity<List<Project>> searchProjects(
        @RequestParam String keyword) {

    return ResponseEntity.ok(
            projectService.searchProjects(keyword)
    );
}
@GetMapping("/category/{category}")
public ResponseEntity<List<Project>> filterByCategory(
        @PathVariable String category) {

    return ResponseEntity.ok(
            projectService.filterByCategory(category)
    );
}
}