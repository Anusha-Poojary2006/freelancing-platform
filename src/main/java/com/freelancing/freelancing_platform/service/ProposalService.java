package com.freelancing.freelancing_platform.service;

import com.freelancing.freelancing_platform.dto.ProposalRequest;
import com.freelancing.freelancing_platform.entity.Project;
import com.freelancing.freelancing_platform.entity.Proposal;
import com.freelancing.freelancing_platform.entity.User;
import com.freelancing.freelancing_platform.repository.ProjectRepository;
import com.freelancing.freelancing_platform.repository.ProposalRepository;
import com.freelancing.freelancing_platform.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProposalService(
            ProposalRepository proposalRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository) {

        this.proposalRepository = proposalRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Proposal createProposal(
            Long projectId,
            ProposalRequest request,
            String email) {

        User freelancer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Only freelancers can submit proposals
        if (freelancer.getRole() != User.Role.FREELANCER) {
            throw new RuntimeException(
                    "Only freelancers can submit proposals");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // Proposal can only be submitted to an open project
        if (project.getStatus() != Project.ProjectStatus.OPEN) {
            throw new RuntimeException(
                    "Proposals can only be submitted to open projects");
        }

        Proposal proposal = new Proposal();

        proposal.setProject(project);
        proposal.setFreelancer(freelancer);
        proposal.setBidAmount(request.getBidAmount());
        proposal.setCoverLetter(request.getCoverLetter());
        proposal.setDeliveryTime(request.getDeliveryTime());

        return proposalRepository.save(proposal);
    }
    public java.util.List<Proposal> getMyProposals(String email) {

    User freelancer = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (freelancer.getRole() != User.Role.FREELANCER) {
        throw new RuntimeException(
                "Only freelancers can view their proposals");
    }

    return proposalRepository.findByFreelancerId(
            freelancer.getId()
    );
}
public java.util.List<Proposal> getProjectProposals(
        Long projectId,
        String email) {

    User client = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // Only clients can view received proposals
    if (client.getRole() != User.Role.CLIENT) {
        throw new RuntimeException(
                "Only clients can view proposals");
    }

    Project project = projectRepository.findById(projectId)
            .orElseThrow(() ->
                    new RuntimeException("Project not found"));

    // Only the project owner can view its proposals
    if (!project.getClient().getId().equals(client.getId())) {
        throw new RuntimeException(
                "You can only view proposals for your own project");
    }

    return proposalRepository.findByProjectId(projectId);
}
public Proposal acceptProposal(Long proposalId, String email) {

    User client = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (client.getRole() != User.Role.CLIENT) {
        throw new RuntimeException(
                "Only clients can accept proposals");
    }

    Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() ->
                    new RuntimeException("Proposal not found"));

    Project project = proposal.getProject();

    if (!project.getClient().getId().equals(client.getId())) {
        throw new RuntimeException(
                "You can only accept proposals for your own project");
    }

    proposal.setStatus(Proposal.ProposalStatus.ACCEPTED);

    // The project moves to IN_PROGRESS after accepting a proposal
    project.setStatus(Project.ProjectStatus.IN_PROGRESS);
    projectRepository.save(project);

    return proposalRepository.save(proposal);
}
public Proposal rejectProposal(Long proposalId, String email) {

    User client = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (client.getRole() != User.Role.CLIENT) {
        throw new RuntimeException(
                "Only clients can reject proposals");
    }

    Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() ->
                    new RuntimeException("Proposal not found"));

    Project project = proposal.getProject();

    if (!project.getClient().getId().equals(client.getId())) {
        throw new RuntimeException(
                "You can only reject proposals for your own project");
    }

    proposal.setStatus(Proposal.ProposalStatus.REJECTED);

    return proposalRepository.save(proposal);
}
public void deleteProposal(Long proposalId, String email) {

    User freelancer = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (freelancer.getRole() != User.Role.FREELANCER) {
        throw new RuntimeException(
                "Only freelancers can delete proposals");
    }

    Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() ->
                    new RuntimeException("Proposal not found"));

    if (!proposal.getFreelancer().getId().equals(freelancer.getId())) {
        throw new RuntimeException(
                "You can only delete your own proposal");
    }

    proposalRepository.delete(proposal);
}
public Project startProject(Long projectId, String email) {

    Project project = projectRepository.findById(projectId)
            .orElseThrow(() ->
                    new RuntimeException("Project not found"));

    User client = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (!project.getClient().getId().equals(client.getId())) {
        throw new RuntimeException(
                "Only the project owner can start the project");
    }

    if (project.getStatus() != Project.ProjectStatus.OPEN) {
        throw new RuntimeException(
                "Only an open project can be started");
    }

    project.setStatus(Project.ProjectStatus.IN_PROGRESS);

    return projectRepository.save(project);
}
public Project completeProject(Long projectId, String email) {

    Project project = projectRepository.findById(projectId)
            .orElseThrow(() ->
                    new RuntimeException("Project not found"));

    User client = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (!project.getClient().getId().equals(client.getId())) {
        throw new RuntimeException(
                "Only the project owner can complete the project");
    }

    if (project.getStatus() != Project.ProjectStatus.IN_PROGRESS) {
        throw new RuntimeException(
                "Only an in-progress project can be completed");
    }

    project.setStatus(Project.ProjectStatus.COMPLETED);

    return projectRepository.save(project);
}
public Project cancelProject(Long projectId, String email) {

    Project project = projectRepository.findById(projectId)
            .orElseThrow(() ->
                    new RuntimeException("Project not found"));

    User client = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (!project.getClient().getId().equals(client.getId())) {
        throw new RuntimeException(
                "Only the project owner can cancel the project");
    }

    if (project.getStatus() == Project.ProjectStatus.COMPLETED) {
        throw new RuntimeException(
                "Completed project cannot be cancelled");
    }

    project.setStatus(Project.ProjectStatus.CANCELLED);

    return projectRepository.save(project);
}
}