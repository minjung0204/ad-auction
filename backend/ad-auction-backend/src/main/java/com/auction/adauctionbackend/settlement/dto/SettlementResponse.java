package com.auction.adauctionbackend.settlement.dto;

import com.auction.adauctionbackend.payment.dto.PaymentResponse;
import com.auction.adauctionbackend.settlement.domain.Settlement;
import com.auction.adauctionbackend.settlement.domain.enums.SettlementStatus;
import com.auction.adauctionbackend.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementResponse {
    private Long id;
    private PaymentResponse payment;
    private UserResponse agency;
    private BigDecimal settlementAmount;
    private String proofScreenshotUrl;
    private SettlementStatus status;
    private LocalDateTime settledAt;
    private LocalDateTime adminCheckedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SettlementResponse from(Settlement settlement) {
        return SettlementResponse.builder()
                .id(settlement.getId())
                .payment(PaymentResponse.from(settlement.getPayment()))
                .agency(UserResponse.from(settlement.getAgency()))
                .settlementAmount(settlement.getSettlementAmount())
                .proofScreenshotUrl(settlement.getProofScreenshotUrl())
                .status(settlement.getStatus())
                .settledAt(settlement.getSettledAt())
                .adminCheckedAt(settlement.getAdminCheckedAt())
                .createdAt(settlement.getCreatedAt())
                .updatedAt(settlement.getUpdatedAt())
                .build();
    }
}