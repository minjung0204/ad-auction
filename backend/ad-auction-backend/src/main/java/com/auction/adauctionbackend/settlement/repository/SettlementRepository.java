package com.auction.adauctionbackend.settlement.repository;

import com.auction.adauctionbackend.settlement.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}