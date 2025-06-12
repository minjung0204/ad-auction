package com.auction.adauctionbackend.bid.dto;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.domain.enums.BidStatus;
import com.auction.adauctionbackend.user.dto.UserResponse;
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
public class BidResponse {
    private Long id;
    private UserResponse advertiser;
    private String placeLink;
    private String desiredRank;
    private String keyword;
    private BidStatus status;
    private LocalDateTime expiresAt;
    private UserResponse selectedAgency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BidResponse from(Bid bid) {
        return BidResponse.builder()
                .id(bid.getId())
                .advertiser(UserResponse.from(bid.getAdvertiser()))
                .placeLink(bid.getPlaceLink())
                .desiredRank(bid.getDesiredRank())
                .keyword(bid.getKeyword())
                .status(bid.getStatus())
                .expiresAt(bid.getExpiresAt())
                .selectedAgency(bid.getSelectedAgency() != null ? UserResponse.from(bid.getSelectedAgency()) : null)
                .createdAt(bid.getCreatedAt())
                .updatedAt(bid.getUpdatedAt())
                .build();
    }
}