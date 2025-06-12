package com.auction.adauctionbackend.advertiser.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidRegistrationRequest {
    @NotBlank(message = "네이버 플레이스 링크는 필수입니다.")
    private String placeLink;

    @NotBlank(message = "희망 노출 순위는 필수입니다.")
    private String desiredRank;

    @NotBlank(message = "희망 키워드는 필수입니다.")
    private String keyword;

    @NotNull(message = "입찰 마감 일시는 필수입니다.")
    @FutureOrPresent(message = "입찰 마감 일시는 현재 또는 미래의 날짜여야 합니다.")
    private LocalDateTime expiresAt;
}