package com.auction.adauctionbackend.proposal.controller;

import com.auction.adauctionbackend.proposal.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    // 여기에 제안 관련 API 엔드포인트를 구현합니다.
}