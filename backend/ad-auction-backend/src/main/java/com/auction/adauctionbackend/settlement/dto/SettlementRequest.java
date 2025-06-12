package com.auction.adauctionbackend.settlement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettlementRequest {
    private Long paymentId;
    private BigDecimal requestedAmount; // The amount the agency requests to be settled
    private String proofScreenshotUrl;
}