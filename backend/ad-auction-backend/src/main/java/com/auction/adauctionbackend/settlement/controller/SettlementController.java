package com.auction.adauctionbackend.settlement.controller;

import com.auction.adauctionbackend.settlement.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @Autowired
    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    // 여기에 정산 관련 API 엔드포인트를 구현합니다.
}