package com.auction.adauctionbackend.proposal.service;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.domain.enums.BidStatus;
import com.auction.adauctionbackend.bid.repository.BidRepository;
import com.auction.adauctionbackend.agency.dto.ProposalSubmissionRequest;
import com.auction.adauctionbackend.agency.dto.ProposalUpdateRequest;
import com.auction.adauctionbackend.proposal.domain.Proposal;
import com.auction.adauctionbackend.proposal.domain.enums.ProposalStatus;
import com.auction.adauctionbackend.proposal.repository.ProposalRepository;
import com.auction.adauctionbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final BidRepository bidRepository;

    /**
     * 특정 입찰에 대해 특정 대행사가 제출한 제안을 조회합니다.
     * 
     * @param bid    조회할 입찰 엔티티
     * @param agency 제안을 조회할 대행사 사용자 엔티티
     * @return 조회된 제안 엔티티 (Optional)
     */
    public Optional<Proposal> getProposalByBidAndAgency(Bid bid, User agency) {
        return proposalRepository.findByBidAndAgency(bid, agency);
    }

    /**
     * 특정 입찰에 대한 모든 제안을 조회합니다.
     * 
     * @param bid 조회할 입찰 엔티티
     * @return 해당 입찰에 대한 모든 제안 목록
     */
    public List<Proposal> getProposalsByBid(Bid bid) {
        return proposalRepository.findByBid(bid);
    }

    /**
     * 대행사가 특정 입찰에 대한 새로운 제안을 제출합니다.
     * 
     * @param bidId   제안을 제출할 입찰의 ID
     * @param request 제안 제출 요청 DTO
     * @param agency  제안을 제출하는 대행사 사용자 엔티티
     * @return 생성된 제안 엔티티
     * @throws ResponseStatusException 입찰을 찾을 수 없거나, 유효하지 않은 입찰 상태, 이미 제안이 제출된 경우 발생
     */
    @Transactional
    public Proposal submitProposal(Long bidId, ProposalSubmissionRequest request, User agency) {
        // 1. 입찰 유효성 확인
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "입찰을 찾을 수 없습니다."));

        // 2. 입찰 상태 확인 (ACTIVE 또는 PENDING 상태에서만 제안 가능)
        if (!bid.getStatus().equals(BidStatus.ACTIVE) && !bid.getStatus().equals(BidStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 상태에서는 제안을 제출할 수 없습니다.");
        }

        // 3. 이미 해당 입찰에 대한 대행사의 제안이 있는지 확인
        if (proposalRepository.findByBidAndAgency(bid, agency).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 해당 입찰에 제안을 제출했습니다.");
        }

        // 4. 제안 엔티티 생성
        Proposal newProposal = Proposal.builder()
                .bid(bid)
                .agency(agency)
                .proposedPrice(request.getProposedPrice())
                .status(ProposalStatus.SUBMITTED) // 초기 상태는 SUBMITTED
                .build();

        return proposalRepository.save(newProposal);
    }

    /**
     * 대행사가 제출한 제안을 수정합니다.
     * 제안은 SUBMITTED 상태일 때만 수정 가능하며, 이미 선정되었거나 거절된 제안은 수정할 수 없습니다.
     * 
     * @param proposalId 수정할 제안의 ID
     * @param request    제안 수정 요청 DTO
     * @param agency     제안을 수정하는 대행사 사용자 엔티티
     * @return 수정된 제안 엔티티
     * @throws ResponseStatusException 제안을 찾을 수 없거나, 수정 권한이 없거나, 수정할 수 없는 상태인 경우 발생
     */
    @Transactional
    public Proposal updateProposal(Long proposalId, ProposalUpdateRequest request, User agency) {
        // 1. 제안 조회 및 소유자 확인
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "제안을 찾을 수 없습니다."));

        if (!proposal.getAgency().getId().equals(agency.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 제안에 대한 수정 권한이 없습니다.");
        }

        // 2. 제안 상태 확인 (SUBMITTED 상태만 수정 가능)
        if (!proposal.getStatus().equals(ProposalStatus.SUBMITTED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 처리된 제안은 수정할 수 없습니다.");
        }

        // 3. 제안 가격 업데이트
        if (request.getProposedPrice() != null) {
            proposal.setProposedPrice(request.getProposedPrice());
        }

        // 4. 제안 상태 업데이트 (요청이 있을 경우에만)
        if (request.getStatus() != null && !request.getStatus().equals(proposal.getStatus())) {
            // 상태 변경에 대한 추가적인 비즈니스 로직 및 유효성 검사 필요 시 여기에 추가
            // 예: SUBMITTED -> MODIFIED로만 변경 가능하도록 제한 등
            proposal.setStatus(request.getStatus());
        }

        return proposalRepository.save(proposal);
    }
}