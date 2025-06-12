package com.auction.adauctionbackend.proposal.service;

import com.auction.adauctionbackend.proposal.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    // 여기에 제안 관련 비즈니스 로직을 구현합니다.
}