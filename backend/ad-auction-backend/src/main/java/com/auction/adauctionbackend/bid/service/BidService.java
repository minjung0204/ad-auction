package com.auction.adauctionbackend.bid.service;

import com.auction.adauctionbackend.bid.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    // 여기에 입찰 관련 비즈니스 로직을 구현합니다.
}