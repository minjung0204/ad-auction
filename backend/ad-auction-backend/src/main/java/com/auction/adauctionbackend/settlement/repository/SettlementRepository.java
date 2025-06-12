package com.auction.adauctionbackend.settlement.repository;

import com.auction.adauctionbackend.settlement.domain.Settlement;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByAgency(User agency);
}