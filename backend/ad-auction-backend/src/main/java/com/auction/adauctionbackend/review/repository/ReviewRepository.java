package com.auction.adauctionbackend.review.repository;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.review.domain.Review;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByBidAndAdvertiser(Bid bid, User advertiser);

    List<Review> findByAdvertiser(User advertiser);
}