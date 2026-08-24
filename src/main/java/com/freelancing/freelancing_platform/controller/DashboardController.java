package com.freelancing.freelancing_platform.controller;

import com.freelancing.freelancing_platform.entity.Project;
import com.freelancing.freelancing_platform.entity.Proposal;
import com.freelancing.freelancing_platform.entity.User;
import com.freelancing.freelancing_platform.repository.ProjectRepository;
import com.freelancing.freelancing_platform.repository.ProposalRepository;
import com.freelancing.freelancing_platform.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProposalRepository proposalRepository;

    public DashboardController(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ProposalRepository proposalRepository) {

        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.proposalRepository = proposalRepository;
    }

    // CLIENT DASHBOARD
    @GetMapping("/client/dashboard")
    public ResponseEntity<Map<String, Object>> clientDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        User client = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (client.getRole() != User.Role.CLIENT) {
            throw new RuntimeException(
                    "Only clients can access this dashboard");
        }

        List<Project> projects =
                projectRepository.findByClientId(client.getId());

        long totalProjects = projects.size();

        long activeProjects = projects.stream()
                .filter(project ->
                        project.getStatus() ==
                                Project.ProjectStatus.IN_PROGRESS)
                .count();

        long completedProjects = projects.stream()
                .filter(project ->
                        project.getStatus() ==
                                Project.ProjectStatus.COMPLETED)
                .count();

        long totalProposalsReceived = 0;

        for (Project project : projects) {

            totalProposalsReceived +=
                    proposalRepository
                            .findByProjectId(project.getId())
                            .size();
        }

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalProjects", totalProjects);
        dashboard.put("activeProjects", activeProjects);
        dashboard.put("completedProjects", completedProjects);
        dashboard.put(
                "totalProposalsReceived",
                totalProposalsReceived
        );

        return ResponseEntity.ok(dashboard);
    }

    // FREELANCER DASHBOARD
    @GetMapping("/freelancer/dashboard")
    public ResponseEntity<Map<String, Object>> freelancerDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        User freelancer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (freelancer.getRole() != User.Role.FREELANCER) {
            throw new RuntimeException(
                    "Only freelancers can access this dashboard");
        }

        List<Proposal> proposals =
                proposalRepository.findByFreelancerId(
                        freelancer.getId());

        long appliedProjects = proposals.stream()
                .map(proposal ->
                        proposal.getProject().getId())
                .distinct()
                .count();

        long activeProjects = proposals.stream()
                .filter(proposal ->
                        proposal.getProject().getStatus() ==
                                Project.ProjectStatus.IN_PROGRESS)
                .map(proposal ->
                        proposal.getProject().getId())
                .distinct()
                .count();

        long completedProjects = proposals.stream()
                .filter(proposal ->
                        proposal.getProject().getStatus() ==
                                Project.ProjectStatus.COMPLETED)
                .map(proposal ->
                        proposal.getProject().getId())
                .distinct()
                .count();

        long acceptedProposals = proposals.stream()
                .filter(proposal ->
                        proposal.getStatus() ==
                                Proposal.ProposalStatus.ACCEPTED)
                .count();

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("appliedProjects", appliedProjects);
        dashboard.put("activeProjects", activeProjects);
        dashboard.put("completedProjects", completedProjects);
        dashboard.put("acceptedProposals", acceptedProposals);

        return ResponseEntity.ok(dashboard);
    }
}