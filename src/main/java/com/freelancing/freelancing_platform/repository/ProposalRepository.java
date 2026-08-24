package com.freelancing.freelancing_platform.repository;

import com.freelancing.freelancing_platform.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository
        extends JpaRepository<Proposal, Long> {

    List<Proposal> findByFreelancerId(Long freelancerId);

    List<Proposal> findByProjectId(Long projectId);
}