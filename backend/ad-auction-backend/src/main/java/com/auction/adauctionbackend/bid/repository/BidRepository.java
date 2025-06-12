package com.auction.adauctionbackend.bid.repository;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    // 특정 광고주가 등록한 모든 입찰 요청 목록을 조회합니다.
    List<Bid> findByAdvertiser(User advertiser);

    // 특정 광고주의 특정 입찰 요청을 상세 조회합니다.
    Optional<Bid> findByIdAndAdvertiser(Long bidId, User advertiser);
}