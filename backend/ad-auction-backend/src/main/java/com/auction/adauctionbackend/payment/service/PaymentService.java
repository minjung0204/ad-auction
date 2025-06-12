package com.auction.adauctionbackend.payment.service;

import com.auction.adauctionbackend.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // 여기에 결제 관련 비즈니스 로직을 구현합니다.
}