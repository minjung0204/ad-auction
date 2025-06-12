package com.auction.adauctionbackend.proposal.repository;

import com.auction.adauctionbackend.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}