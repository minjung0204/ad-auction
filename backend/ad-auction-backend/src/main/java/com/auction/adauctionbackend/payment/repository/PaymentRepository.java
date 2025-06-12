package com.auction.adauctionbackend.payment.repository;

import com.auction.adauctionbackend.payment.domain.Payment;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 특정 광고주가 생성한 모든 결제 내역을 조회합니다.
    List<Payment> findByAdvertiser(User advertiser);
}