package com.auction.adauctionbackend.payment.domain.enums;

public enum PaymentStatus {
    PENDING, // 결제 대기 중
    COMPLETED, // 결제 완료
    REFUNDED // 환불됨
}