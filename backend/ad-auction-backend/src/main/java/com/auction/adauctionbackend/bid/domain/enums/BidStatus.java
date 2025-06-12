package com.auction.adauctionbackend.bid.domain.enums;

public enum BidStatus {
    PENDING, // 입찰 요청 대기 중
    ACTIVE, // 입찰 진행 중
    CLOSED, // 입찰 마감 (기간 만료)
    SELECTED, // 대행사 선정 완료
    CANCELED // 입찰 취소 (광고주에 의해)
}