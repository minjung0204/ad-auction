package com.auction.adauctionbackend.review.service;

import com.auction.adauctionbackend.advertiser.dto.ReviewCreationRequest;
import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.domain.enums.BidStatus;
import com.auction.adauctionbackend.bid.repository.BidRepository;
import com.auction.adauctionbackend.review.domain.Review;
import com.auction.adauctionbackend.review.domain.enums.ReviewStatus;
import com.auction.adauctionbackend.review.repository.ReviewRepository;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 리뷰를 작성합니다.
     * 광고주가 특정 입찰을 통해 선정된 대행사에 대해서만 리뷰를 작성할 수 있습니다.
     * 
     * @param request    리뷰 작성 요청 DTO
     * @param advertiser 리뷰를 작성하는 광고주 사용자 엔티티
     * @return 생성된 리뷰 엔티티
     * @throws ResponseStatusException 입찰, 대행사, 또는 권한 문제 발생 시
     */
    @Transactional
    public Review createReview(ReviewCreationRequest request, User advertiser) {
        // 1. 입찰 유효성 확인
        Bid bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 입찰을 찾을 수 없습니다."));

        // 2. 대행사 유효성 확인
        User agency = userRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 대행사를 찾을 수 없습니다."));

        // 3. 광고주가 해당 입찰의 소유자인지 확인
        if (!bid.getAdvertiser().getId().equals(advertiser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 입찰에 대한 리뷰 작성 권한이 없습니다.");
        }

        // 4. 입찰이 'SELECTED' 상태이고, 현재 광고주가 해당 대행사를 선택했는지 확인
        if (!bid.getStatus().equals(BidStatus.SELECTED) || !bid.getSelectedAgency().getId().equals(agency.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "선정된 대행사에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        // 5. 이미 해당 입찰에 대한 리뷰를 작성했는지 확인
        if (reviewRepository.findByBidAndAdvertiser(bid, advertiser).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 이 입찰에 대한 리뷰를 작성했습니다.");
        }

        // 6. 리뷰 엔티티 생성
        Review newReview = Review.builder()
                .advertiser(advertiser)
                .agency(agency)
                .bid(bid)
                .rating(request.getRating())
                .comment(request.getComment())
                .status(ReviewStatus.ACTIVE) // 초기 상태는 ACTIVE
                .build();

        return reviewRepository.save(newReview);
    }

    /**
     * 특정 광고주가 작성한 모든 리뷰 목록을 조회합니다.
     * 
     * @param advertiser 리뷰를 조회할 광고주 사용자 엔티티
     * @return 해당 광고주의 리뷰 목록
     */
    public List<Review> getReviewsByAdvertiser(User advertiser) {
        return reviewRepository.findByAdvertiser(advertiser);
    }
}