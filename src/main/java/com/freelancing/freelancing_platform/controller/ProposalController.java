package com.freelancing.freelancing_platform.controller;

import com.freelancing.freelancing_platform.dto.ProposalRequest;
import com.freelancing.freelancing_platform.entity.Proposal;
import com.freelancing.freelancing_platform.service.ProposalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping("/{projectId}/proposal")
    public ResponseEntity<Proposal> createProposal(
            @PathVariable Long projectId,
            @RequestBody @Valid ProposalRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Proposal proposal = proposalService.createProposal(
                projectId,
                request,
                email
        );

        return ResponseEntity.ok(proposal);
    }

    @GetMapping("/my-proposals")
    public ResponseEntity<List<Proposal>> getMyProposals(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                proposalService.getMyProposals(email)
        );
    }

    @GetMapping("/{projectId}/proposals")
    public ResponseEntity<List<Proposal>> getProjectProposals(
            @PathVariable Long projectId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                proposalService.getProjectProposals(
                        projectId,
                        email
                )
        );
    }

    @PutMapping("/proposal/{proposalId}/accept")
    public ResponseEntity<Proposal> acceptProposal(
            @PathVariable Long proposalId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                proposalService.acceptProposal(
                        proposalId,
                        email
                )
        );
    }

    @PutMapping("/proposal/{proposalId}/reject")
    public ResponseEntity<Proposal> rejectProposal(
            @PathVariable Long proposalId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                proposalService.rejectProposal(
                        proposalId,
                        email
                )
        );
    }

    @DeleteMapping("/proposal/{proposalId}")
    public ResponseEntity<String> deleteProposal(
            @PathVariable Long proposalId,
            Authentication authentication) {

        String email = authentication.getName();

        proposalService.deleteProposal(
                proposalId,
                email
        );

        return ResponseEntity.ok(
                "Proposal deleted successfully"
        );
    }
}