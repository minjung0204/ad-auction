package com.auction.adauctionbackend.settlement.service;

import com.auction.adauctionbackend.settlement.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;

    @Autowired
    public SettlementService(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    // 여기에 정산 관련 비즈니스 로직을 구현합니다.
}