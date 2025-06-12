package com.auction.backend.agency.controller;

import com.auction.backend.agency.dto.AgencyBidDetailsResponse;
import com.auction.backend.bid.service.BidService;
import com.auction.backend.portfolio.dto.PortfolioCreationRequest;
import com.auction.backend.portfolio.dto.PortfolioResponse;
import com.auction.backend.portfolio.dto.PortfolioUpdateRequest;
import com.auction.backend.portfolio.service.PortfolioService;
import com.auction.backend.proposal.dto.ProposalResponse;
import com.auction.backend.proposal.dto.ProposalSubmissionRequest;
import com.auction.backend.proposal.dto.ProposalUpdateRequest;
import com.auction.backend.proposal.service.ProposalService;
import com.auction.backend.user.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final BidService bidService;
    private final ProposalService proposalService;
    private final PortfolioService portfolioService;

    // 입찰 목록 조회 (대행사용)
    @GetMapping("/bids")
    public ResponseEntity<List<AgencyBidDetailsResponse>> getAllBids(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AgencyBidDetailsResponse> bids = bidService.getAllActiveBids();
        return ResponseEntity.ok(bids);
    }

    // 특정 입찰 상세 조회 (대행사용)
    @GetMapping("/bids/{bid_id}")
    public ResponseEntity<AgencyBidDetailsResponse> getBidDetails(@PathVariable("bid_id") Long bidId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AgencyBidDetailsResponse bidDetails = bidService.getBidById(bidId, userDetails.getId());
        return ResponseEntity.ok(bidDetails);
    }

    // 입찰 제안 제출
    @PostMapping("/bids/{bid_id}/proposals")
    public ResponseEntity<ProposalResponse> submitProposal(@PathVariable("bid_id") Long bidId,
            @RequestBody ProposalSubmissionRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProposalResponse response = proposalService.submitProposal(bidId, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 입찰 제안 수정
    @PutMapping("/proposals/{proposal_id}")
    public ResponseEntity<ProposalResponse> updateProposal(@PathVariable("proposal_id") Long proposalId,
            @RequestBody ProposalUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProposalResponse response = proposalService.updateProposal(proposalId, userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    // 포트폴리오 등록
    @PostMapping("/portfolios")
    public ResponseEntity<PortfolioResponse> createPortfolio(@RequestBody PortfolioCreationRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioResponse response = portfolioService.createPortfolio(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 포트폴리오 수정
    @PutMapping("/portfolios/{portfolio_id}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable("portfolio_id") Long portfolioId,
            @RequestBody PortfolioUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioResponse response = portfolioService.updatePortfolio(portfolioId, request, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    // 내 포트폴리오 목록 조회
    @GetMapping("/portfolios")
    public ResponseEntity<List<PortfolioResponse>> getMyPortfolios(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PortfolioResponse> portfolios = portfolioService.getPortfoliosByAgency(userDetails.getId());
        return ResponseEntity.ok(portfolios);
    }

    // 특정 포트폴리오 상세 조회
    @GetMapping("/portfolios/{portfolio_id}")
    public ResponseEntity<PortfolioResponse> getPortfolioById(@PathVariable("portfolio_id") Long portfolioId) {
        PortfolioResponse portfolio = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(portfolio);
    }

    // 포트폴리오 삭제
    @DeleteMapping("/portfolios/{portfolio_id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable("portfolio_id") Long portfolioId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        portfolioService.deletePortfolio(portfolioId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}