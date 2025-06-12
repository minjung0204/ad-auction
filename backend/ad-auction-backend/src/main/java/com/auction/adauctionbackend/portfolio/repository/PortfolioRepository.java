package com.auction.adauctionbackend.portfolio.repository;

import com.auction.adauctionbackend.portfolio.domain.Portfolio;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByAgency(User agency);
}