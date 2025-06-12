package com.auction.adauctionbackend.review.dto;

import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.review.domain.Review;
import com.auction.adauctionbackend.review.domain.enums.ReviewStatus;
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
public class ReviewResponse {
    private Long id;
    private UserResponse advertiser;
    private UserResponse agency;
    private BidResponse bid;
    private Integer rating;
    private String comment;
    private ReviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .advertiser(UserResponse.from(review.getAdvertiser()))
                .agency(UserResponse.from(review.getAgency()))
                .bid(review.getBid() != null ? BidResponse.from(review.getBid()) : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}