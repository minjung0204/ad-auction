package com.auction.adauctionbackend.portfolio.service;

import com.auction.adauctionbackend.portfolio.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    // 여기에 포트폴리오 관련 비즈니스 로직을 구현합니다.
}