package com.auction.adauctionbackend.agency.controller;

import com.auction.adauctionbackend.agency.dto.AgencyBidDetailsResponse;
import com.auction.adauctionbackend.agency.dto.ProposalSubmissionRequest;
import com.auction.adauctionbackend.agency.dto.ProposalUpdateRequest;
import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.bid.service.BidService;
import com.auction.adauctionbackend.proposal.domain.Proposal;
import com.auction.adauctionbackend.proposal.dto.ProposalResponse;
import com.auction.adauctionbackend.proposal.service.ProposalService;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final BidService bidService;
    private final UserService userService;
    private final ProposalService proposalService;

    /**
     * 대행사가 조회할 수 있는 모든 활성 입찰 요청 목록을 조회합니다.
     * 
     * @return 활성 입찰 요청 목록을 담은 응답 엔티티
     */
    @GetMapping("/bids")
    public ResponseEntity<List<BidResponse>> getAllBids() {
        List<BidResponse> bidResponses = bidService.getAllActiveBids().stream()
                .map(BidResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bidResponses);
    }

    /**
     * 대행사가 특정 입찰 요청의 상세 정보를 조회합니다.
     * 해당 입찰에 대한 대행사의 제안 정보도 함께 포함됩니다 (있는 경우).
     * 
     * @param bidId 조회할 입찰의 ID
     * @return 입찰 상세 정보 및 제안 정보를 담은 응답 엔티티
     * @throws ResponseStatusException 입찰을 찾을 수 없을 경우 발생
     */
    @GetMapping("/bids/{bid_id}")
    public ResponseEntity<AgencyBidDetailsResponse> getBidDetailsForAgency(@PathVariable("bid_id") Long bidId) {
        // 1. 입찰 정보 조회
        Bid bid = bidService.getBidById(bidId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "입찰을 찾을 수 없습니다."));

        // 2. 현재 로그인된 대행사 정보 가져오기
        User currentAgency = userService.getCurrentUser();

        // 3. 해당 입찰에 대한 현재 대행사의 제안 조회 (선택 사항)
        Optional<Proposal> agencyProposalOptional = proposalService.getProposalByBidAndAgency(bid, currentAgency);
        ProposalResponse agencyProposalResponse = agencyProposalOptional.map(ProposalResponse::from).orElse(null);

        // 4. 응답 DTO 생성 및 반환
        AgencyBidDetailsResponse response = AgencyBidDetailsResponse.builder()
                .bid(BidResponse.from(bid))
                .agencyProposal(agencyProposalResponse)
                .build();

        return ResponseEntity.ok(response);
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
            @Valid @RequestBody ProposalSubmissionRequest request) {
        User currentAgency = userService.getCurrentUser();
        Proposal newProposal = proposalService.submitProposal(bidId, request, currentAgency);
        return new ResponseEntity<>(ProposalResponse.from(newProposal), HttpStatus.CREATED);
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
            @Valid @RequestBody ProposalUpdateRequest request) {
        User currentAgency = userService.getCurrentUser();
        Proposal updatedProposal = proposalService.updateProposal(proposalId, request, currentAgency);
        return ResponseEntity.ok(ProposalResponse.from(updatedProposal));
    }
}