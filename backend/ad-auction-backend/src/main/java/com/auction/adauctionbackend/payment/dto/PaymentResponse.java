package com.auction.adauctionbackend.payment.dto;

import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.payment.domain.Payment;
import com.auction.adauctionbackend.payment.domain.enums.PaymentStatus;
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
public class PaymentResponse {
    private Long id;
    private BidResponse bid;
    private UserResponse advertiser;
    private UserResponse agency;
    private BigDecimal amount;
    private BigDecimal platformFee;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bid(BidResponse.from(payment.getBid()))
                .advertiser(UserResponse.from(payment.getAdvertiser()))
                .agency(payment.getAgency() != null ? UserResponse.from(payment.getAgency()) : null)
                .amount(payment.getAmount())
                .platformFee(payment.getPlatformFee())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}