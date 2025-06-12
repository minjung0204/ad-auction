package com.auction.adauctionbackend.settlement.service;

import com.auction.adauctionbackend.payment.domain.Payment;
import com.auction.adauctionbackend.payment.repository.PaymentRepository;
import com.auction.adauctionbackend.settlement.domain.Settlement;
import com.auction.adauctionbackend.settlement.domain.enums.SettlementStatus;
import com.auction.adauctionbackend.settlement.dto.SettlementRequest;
import com.auction.adauctionbackend.settlement.dto.SettlementResponse;
import com.auction.adauctionbackend.settlement.repository.SettlementRepository;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    // 정산 요청
    @Transactional
    public SettlementResponse requestSettlement(SettlementRequest request, User agency) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Payment not found with id: " + request.getPaymentId()));

        if (!payment.getAgency().getId().equals(agency.getId())) {
            throw new SecurityException("You are not authorized to request settlement for this payment.");
        }

        // TODO: 이미 정산 요청이 존재하는지 확인 (중복 요청 방지)
        // TODO: payment.status가 'COMPLETED' (결제 완료) 상태인지 확인

        Settlement settlement = Settlement.builder()
                .payment(payment)
                .agency(agency)
                .settlementAmount(request.getRequestedAmount())
                .proofScreenshotUrl(request.getProofScreenshotUrl())
                .status(SettlementStatus.PENDING)
                .build();

        return SettlementResponse.from(settlementRepository.save(settlement));
    }

    // 대행사별 정산 내역 조회
    @Transactional(readOnly = true)
    public List<SettlementResponse> getSettlementsByAgencyId(User agency) {
        return settlementRepository.findByAgency(agency).stream()
                .map(SettlementResponse::from)
                .collect(Collectors.toList());
    }
}