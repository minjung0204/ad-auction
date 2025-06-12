package com.auction.adauctionbackend.payment.service;

import com.auction.adauctionbackend.payment.domain.Payment;
import com.auction.adauctionbackend.payment.repository.PaymentRepository;
import com.auction.adauctionbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 특정 광고주가 생성한 모든 결제 내역 목록을 조회합니다.
     * 
     * @param advertiser 결제 내역을 조회할 광고주 사용자 엔티티
     * @return 해당 광고주의 결제 내역 목록
     */
    public List<Payment> getPaymentsByAdvertiser(User advertiser) {
        return paymentRepository.findByAdvertiser(advertiser);
    }

    // 여기에 결제 관련 비즈니스 로직을 구현합니다.
}