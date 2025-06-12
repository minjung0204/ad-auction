package com.auction.adauctionbackend.agency.controller;

import com.auction.adauctionbackend.agency.dto.AgencyBidDetailsResponse;
import com.auction.adauctionbackend.bid.service.BidService;
import com.auction.adauctionbackend.portfolio.dto.PortfolioCreationRequest;
import com.auction.adauctionbackend.portfolio.dto.PortfolioResponse;
import com.auction.adauctionbackend.portfolio.dto.PortfolioUpdateRequest;
import com.auction.adauctionbackend.portfolio.service.PortfolioService;
import com.auction.adauctionbackend.proposal.dto.ProposalResponse;
import com.auction.adauctionbackend.proposal.dto.ProposalSubmissionRequest;
import com.auction.adauctionbackend.proposal.dto.ProposalUpdateRequest;
import com.auction.adauctionbackend.proposal.service.ProposalService;
import com.auction.adauctionbackend.settlement.dto.SettlementRequest;
import com.auction.adauctionbackend.settlement.dto.SettlementResponse;
import com.auction.adauctionbackend.settlement.service.SettlementService;
import com.auction.adauctionbackend.user.config.UserDetailsImpl;
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
    private final SettlementService settlementService;

    /**
     * 대행사가 조회할 수 있는 모든 활성 입찰 요청 목록을 조회합니다.
     * 
     * @return 활성 입찰 요청 목록을 담은 응답 엔티티
     */
    @GetMapping("/bids")
    public ResponseEntity<List<AgencyBidDetailsResponse>> getAllBids(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AgencyBidDetailsResponse> bids = bidService.getAllActiveBids();
        return ResponseEntity.ok(bids);
    }

    /**
     * 대행사가 특정 입찰 요청의 상세 정보를 조회합니다.
     * 해당 입찰에 대한 대행사의 제안 정보도 함께 포함됩니다 (있는 경우).
     * 
     * @param bidId 조회할 입찰의 ID
     * @return 입찰 상세 정보 및 제안 정보를 담은 응답 엔티티
     */
    @GetMapping("/bids/{bid_id}")
    public ResponseEntity<AgencyBidDetailsResponse> getBidDetails(@PathVariable("bid_id") Long bidId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AgencyBidDetailsResponse bidDetails = bidService.getBidById(bidId, userDetails.getId());
        return ResponseEntity.ok(bidDetails);
    }

    /**
     * 대행사가 특정 입찰에 대한 제안을 제출합니다.
     * 
     * @param bidId   제안을 제출할 입찰의 ID
     * @param request 제안 제출 요청 DTO
     * @return 제출된 제안 정보를 담은 응답 엔티티
     */
    @PostMapping("/bids/{bid_id}/proposals")
    public ResponseEntity<ProposalResponse> submitProposal(@PathVariable("bid_id") Long bidId,
            @RequestBody ProposalSubmissionRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProposalResponse response = proposalService.submitProposal(bidId, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 대행사가 제출한 제안을 수정합니다.
     * 
     * @param proposalId 수정할 제안의 ID
     * @param request    제안 수정 요청 DTO
     * @return 수정된 제안 정보를 담은 응답 엔티티
     */
    @PutMapping("/proposals/{proposal_id}")
    public ResponseEntity<ProposalResponse> updateProposal(@PathVariable("proposal_id") Long proposalId,
            @RequestBody ProposalUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProposalResponse response = proposalService.updateProposal(proposalId, userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * 포트폴리오 등록
     * 
     * @param request 포트폴리오 등록 요청 DTO
     * @return 등록된 포트폴리오 정보를 담은 응답 엔티티
     */
    @PostMapping("/portfolios")
    public ResponseEntity<PortfolioResponse> createPortfolio(@RequestBody PortfolioCreationRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioResponse response = portfolioService.createPortfolio(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 포트폴리오 수정
     * 
     * @param portfolioId 수정할 포트폴리오의 ID
     * @param request     포트폴리오 수정 요청 DTO
     * @return 수정된 포트폴리오 정보를 담은 응답 엔티티
     */
    @PutMapping("/portfolios/{portfolio_id}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable("portfolio_id") Long portfolioId,
            @RequestBody PortfolioUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioResponse response = portfolioService.updatePortfolio(portfolioId, request, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * 내 포트폴리오 목록 조회
     * 
     * @return 내 포트폴리오 목록을 담은 응답 엔티티
     */
    @GetMapping("/portfolios")
    public ResponseEntity<List<PortfolioResponse>> getMyPortfolios(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PortfolioResponse> portfolios = portfolioService.getPortfoliosByAgency(userDetails.getId());
        return ResponseEntity.ok(portfolios);
    }

    /**
     * 특정 포트폴리오 상세 조회
     * 
     * @param portfolioId 조회할 포트폴리오의 ID
     * @return 포트폴리오 정보를 담은 응답 엔티티
     */
    @GetMapping("/portfolios/{portfolio_id}")
    public ResponseEntity<PortfolioResponse> getPortfolioById(@PathVariable("portfolio_id") Long portfolioId) {
        PortfolioResponse portfolio = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(portfolio);
    }

    /**
     * 포트폴리오 삭제
     * 
     * @param portfolioId 삭제할 포트폴리오의 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/portfolios/{portfolio_id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable("portfolio_id") Long portfolioId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        portfolioService.deletePortfolio(portfolioId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * 정산 요청
     * 
     * @param request 정산 요청 DTO
     * @return 정산 응답 DTO
     */
    @PostMapping("/settlements")
    public ResponseEntity<SettlementResponse> requestSettlement(@RequestBody SettlementRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        SettlementResponse response = settlementService.requestSettlement(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내 정산 내역 조회
     * 
     * @return 내 정산 내역을 담은 응답 엔티티
     */
    @GetMapping("/settlements")
    public ResponseEntity<List<SettlementResponse>> getMySettlements(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SettlementResponse> settlements = settlementService.getSettlementsByAgencyId(userDetails.getId());
        return ResponseEntity.ok(settlements);
    }
}