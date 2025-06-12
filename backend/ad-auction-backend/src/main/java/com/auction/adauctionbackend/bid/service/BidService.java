package com.auction.adauctionbackend.bid.service;

import com.auction.adauctionbackend.advertiser.dto.BidRegistrationRequest;
import com.auction.adauctionbackend.agency.dto.AgencyBidDetailsResponse;
import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.domain.enums.BidStatus;
import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.bid.repository.BidRepository;
import com.auction.adauctionbackend.proposal.domain.Proposal;
import com.auction.adauctionbackend.proposal.domain.enums.ProposalStatus;
import com.auction.adauctionbackend.proposal.dto.ProposalResponse;
import com.auction.adauctionbackend.proposal.repository.ProposalRepository;
import com.auction.adauctionbackend.proposal.service.ProposalService;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalService proposalService;
    private final UserService userService;

    /**
     * 새로운 입찰 요청을 등록합니다. 초기 상태는 PENDING입니다.
     * 
     * @param request    입찰 등록 요청 DTO
     * @param advertiser 입찰을 등록하는 광고주 사용자 엔티티
     * @return 등록된 입찰 엔티티
     */
    @Transactional
    public Bid createBid(BidRegistrationRequest request, User advertiser) {
        // 입찰 엔티티 생성
        Bid newBid = Bid.builder()
                .advertiser(advertiser)
                .placeLink(request.getPlaceLink())
                .desiredRank(request.getDesiredRank())
                .keyword(request.getKeyword())
                .status(BidStatus.PENDING) // 초기 상태는 PENDING
                .expiresAt(request.getExpiresAt())
                .build();

        return bidRepository.save(newBid);
    }

    /**
     * 특정 광고주가 등록한 모든 입찰 요청 목록을 조회합니다.
     * 
     * @param advertiser 입찰 요청을 조회할 광고주 사용자 엔티티
     * @return 해당 광고주의 입찰 요청 목록
     */
    public List<Bid> getBidsByAdvertiser(User advertiser) {
        return bidRepository.findByAdvertiser(advertiser);
    }

    /**
     * 특정 광고주의 특정 입찰 요청을 상세 조회합니다.
     * 
     * @param bidId      조회할 입찰의 ID
     * @param advertiser 입찰을 등록한 광고주 사용자 엔티티
     * @return 조회된 입찰 엔티티 (Optional)
     */
    public Optional<Bid> getBidByIdAndAdvertiser(Long bidId, User advertiser) {
        return bidRepository.findByIdAndAdvertiser(bidId, advertiser);
    }

    /**
     * 광고주가 특정 입찰에 대한 대행사 제안을 선정합니다.
     * 
     * @param bidId      대행사를 선정할 입찰 ID
     * @param proposalId 선정할 제안의 ID
     * @param advertiser 현재 로그인된 광고주 (입찰 소유자 확인용)
     * @return 업데이트된 입찰 엔티티
     * @throws ResponseStatusException 입찰이나 제안을 찾을 수 없거나 권한이 없을 경우
     */
    @Transactional
    public Bid selectAgencyForBid(Long bidId, Long proposalId, User advertiser) {
        // 1. 입찰 조회 및 소유자 확인
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "입찰을 찾을 수 없습니다."));

        if (!bid.getAdvertiser().getId().equals(advertiser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 입찰에 대한 권한이 없습니다.");
        }

        // 2. 이미 대행사가 선정된 입찰인지 확인
        if (bid.getStatus().equals(BidStatus.SELECTED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 대행사가 선정된 입찰입니다.");
        }

        // 3. 제안 조회 및 유효성 확인
        Proposal selectedProposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "제안을 찾을 수 없습니다."));

        if (!selectedProposal.getBid().getId().equals(bidId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 제안은 이 입찰과 관련이 없습니다.");
        }

        // 4. 입찰 상태 업데이트 및 선정된 대행사 설정
        bid.setSelectedAgency(selectedProposal.getAgency());
        bid.setStatus(BidStatus.SELECTED);
        bidRepository.save(bid);

        // 5. 선정된 제안의 상태를 SELECTED로 업데이트
        selectedProposal.setStatus(ProposalStatus.SELECTED);
        proposalRepository.save(selectedProposal);

        // 6. 같은 입찰의 다른 모든 제안을 REJECTED로 업데이트
        List<Proposal> otherProposals = proposalRepository.findByBid(bid);
        for (Proposal proposal : otherProposals) {
            if (!proposal.getId().equals(selectedProposal.getId())) {
                proposal.setStatus(ProposalStatus.REJECTED);
                proposalRepository.save(proposal);
            }
        }

        return bid;
    }

    /**
     * 모든 활성(ACTIVE 또는 PENDING) 입찰 요청 목록을 조회합니다.
     * 대행사에게 노출될 입찰 목록입니다.
     * 
     * @return 활성 입찰 요청 목록
     */
    public List<AgencyBidDetailsResponse> getAllActiveBids() {
        List<Bid> bids = bidRepository.findByStatusIn(List.of(BidStatus.ACTIVE, BidStatus.PENDING));
        return bids.stream()
                .map(bid -> {
                    List<ProposalResponse> proposals = proposalService.getProposalsByBid(bid).stream()
                            .map(ProposalResponse::from)
                            .collect(Collectors.toList());
                    return AgencyBidDetailsResponse.builder()
                            .bid(BidResponse.from(bid))
                            .proposals(proposals)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 입찰 요청을 ID로 조회하고, 해당 입찰에 대한 대행사의 제안 정보를 포함합니다.
     * 
     * @param bidId    조회할 입찰의 ID
     * @param agencyId 현재 로그인된 대행사의 ID (선택 사항, 대행사의 제안을 포함할 때 사용)
     * @return 입찰 상세 정보 및 제안 정보를 담은 DTO (Optional)
     */
    public AgencyBidDetailsResponse getBidById(Long bidId, Long agencyId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "입찰을 찾을 수 없습니다."));

        List<ProposalResponse> proposals = proposalService.getProposalsByBid(bid).stream()
                .filter(proposal -> agencyId == null || proposal.getAgency().getId().equals(agencyId))
                .map(ProposalResponse::from)
                .collect(Collectors.toList());

        return AgencyBidDetailsResponse.builder()
                .bid(BidResponse.from(bid))
                .proposals(proposals)
                .build();
    }

    // 여기에 입찰 관련 비즈니스 로직을 구현합니다.
}