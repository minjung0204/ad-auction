package com.auction.adauctionbackend.advertiser.controller;

import com.auction.adauctionbackend.advertiser.dto.BidRegistrationRequest;
import com.auction.adauctionbackend.advertiser.dto.ReviewCreationRequest;
import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.bid.service.BidService;
import com.auction.adauctionbackend.payment.domain.Payment;
import com.auction.adauctionbackend.payment.dto.PaymentResponse;
import com.auction.adauctionbackend.payment.service.PaymentService;
import com.auction.adauctionbackend.review.domain.Review;
import com.auction.adauctionbackend.review.dto.ReviewResponse;
import com.auction.adauctionbackend.review.service.ReviewService;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/advertisers")
@RequiredArgsConstructor
public class AdvertiserController {

    private final BidService bidService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final ReviewService reviewService;

    /**
     * 광고주가 새로운 입찰 요청을 등록합니다.
     * 
     * @param request 입찰 등록 요청 DTO
     * @return 등록된 입찰 정보를 담은 응답 엔티티
     */
    @PostMapping("/bids")
    public ResponseEntity<BidResponse> registerBid(@Valid @RequestBody BidRegistrationRequest request) {
        // 현재 인증된 광고주 사용자 정보 가져오기
        User currentAdvertiser = userService.getCurrentUser();

        // 입찰 등록 서비스 호출
        Bid newBid = bidService.createBid(request, currentAdvertiser);

        // 응답 DTO 변환 및 반환
        return new ResponseEntity<>(BidResponse.from(newBid), HttpStatus.CREATED);
    }

    /**
     * 현재 로그인된 광고주가 등록한 모든 입찰 요청 목록을 조회합니다.
     * 
     * @return 입찰 요청 목록을 담은 응답 엔티티
     */
    @GetMapping("/bids")
    public ResponseEntity<List<BidResponse>> getMyBids() {
        User currentAdvertiser = userService.getCurrentUser();
        List<Bid> bids = bidService.getBidsByAdvertiser(currentAdvertiser);
        List<BidResponse> bidResponses = bids.stream()
                .map(BidResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bidResponses);
    }

    /**
     * 현재 로그인된 광고주의 특정 입찰 요청을 상세 조회합니다.
     * 
     * @param bidId 조회할 입찰의 ID
     * @return 특정 입찰 정보를 담은 응답 엔티티
     * @throws org.springframework.web.server.ResponseStatusException 해당 입찰을 찾을 수
     *                                                                없거나 접근 권한이 없을
     *                                                                경우 발생
     */
    @GetMapping("/bids/{bid_id}")
    public ResponseEntity<BidResponse> getMyBidDetails(@PathVariable("bid_id") Long bidId) {
        User currentAdvertiser = userService.getCurrentUser();
        Bid bid = bidService.getBidByIdAndAdvertiser(bidId, currentAdvertiser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "입찰을 찾을 수 없거나 접근 권한이 없습니다."));
        return ResponseEntity.ok(BidResponse.from(bid));
    }

    /**
     * 광고주가 특정 입찰에 대한 대행사 제안을 선정합니다.
     * 입찰의 상태가 SELECTED로 변경되고, 선정된 제안의 상태가 SELECTED로, 다른 제안들은 REJECTED로 변경됩니다.
     * 
     * @param bidId   대행사를 선정할 입찰 ID
     * @param request 제안 ID를 포함하는 요청 바디 (예: {"proposalId": 123})
     * @return 업데이트된 입찰 정보를 담은 응답 엔티티
     */
    @PutMapping("/bids/{bid_id}/select")
    public ResponseEntity<BidResponse> selectAgency(@PathVariable("bid_id") Long bidId,
            @RequestBody Map<String, Long> request) {
        Long proposalId = request.get("proposalId");
        if (proposalId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "proposalId는 필수입니다.");
        }

        User currentAdvertiser = userService.getCurrentUser();
        Bid updatedBid = bidService.selectAgencyForBid(bidId, proposalId, currentAdvertiser);
        return ResponseEntity.ok(BidResponse.from(updatedBid));
    }

    /**
     * 현재 로그인된 광고주의 결제 내역 목록을 조회합니다.
     * 
     * @return 결제 내역 목록을 담은 응답 엔티티
     */
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> getMyPayments() {
        User currentAdvertiser = userService.getCurrentUser();
        List<Payment> payments = paymentService.getPaymentsByAdvertiser(currentAdvertiser);
        List<PaymentResponse> paymentResponses = payments.stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentResponses);
    }

    /**
     * 광고주가 대행사에 대한 리뷰를 작성합니다.
     * 
     * @param request 리뷰 생성 요청 DTO
     * @return 생성된 리뷰 정보를 담은 응답 엔티티
     */
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewCreationRequest request) {
        User currentAdvertiser = userService.getCurrentUser();
        Review newReview = reviewService.createReview(request, currentAdvertiser);
        return new ResponseEntity<>(ReviewResponse.from(newReview), HttpStatus.CREATED);
    }

    /**
     * 현재 로그인된 광고주가 작성한 모든 리뷰 목록을 조회합니다.
     * 
     * @return 리뷰 목록을 담은 응답 엔티티
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        User currentAdvertiser = userService.getCurrentUser();
        List<Review> reviews = reviewService.getReviewsByAdvertiser(currentAdvertiser);
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewResponses);
    }
}