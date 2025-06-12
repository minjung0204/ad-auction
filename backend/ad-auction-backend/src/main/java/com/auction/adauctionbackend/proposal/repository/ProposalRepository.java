package com.auction.adauctionbackend.proposal.repository;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.proposal.domain.Proposal;
import com.auction.adauctionbackend.proposal.domain.enums.ProposalStatus;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    // 특정 입찰에 대한 모든 제안을 조회합니다.
    List<Proposal> findByBid(Bid bid);

    // 특정 입찰에 대해 특정 대행사가 제출한 제안을 조회합니다.
    Optional<Proposal> findByBidAndAgency(Bid bid, User agency);

    // 특정 입찰에 대해 선정된 (SELECTED) 제안을 조회합니다.
    Optional<Proposal> findByBidAndStatus(Bid bid, ProposalStatus status);

    // 특정 대행사가 제출한 모든 제안을 조회합니다.
    List<Proposal> findByAgency(User agency);
}