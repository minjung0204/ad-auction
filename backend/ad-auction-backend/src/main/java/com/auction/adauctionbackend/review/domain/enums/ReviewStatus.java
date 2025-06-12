package com.auction.adauctionbackend.review.domain.enums;

public enum ReviewStatus {
    ACTIVE, // 활성 (정상 노출)
    HIDDEN, // 숨김 (관리자에 의해 숨김 처리)
    DELETED // 삭제됨 (광고주에 의해 삭제)
}