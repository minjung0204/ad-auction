package com.auction.adauctionbackend.review.service;

import com.auction.adauctionbackend.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 여기에 리뷰 관련 비즈니스 로직을 구현합니다.
}