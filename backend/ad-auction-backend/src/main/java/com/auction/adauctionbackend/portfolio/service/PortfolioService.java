package com.auction.adauctionbackend.portfolio.service;

import com.auction.adauctionbackend.portfolio.dto.PortfolioCreationRequest;
import com.auction.adauctionbackend.portfolio.dto.PortfolioUpdateRequest;
import com.auction.adauctionbackend.portfolio.domain.Portfolio;
import com.auction.adauctionbackend.portfolio.domain.enums.PortfolioStatus;
import com.auction.adauctionbackend.portfolio.dto.PortfolioResponse;
import com.auction.adauctionbackend.portfolio.repository.PortfolioRepository;
import com.auction.adauctionbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    /**
     * 새로운 포트폴리오를 등록합니다. 초기 상태는 PENDING_APPROVAL입니다.
     * 
     * @param request 포트폴리오 생성 요청 DTO
     * @param agency  포트폴리오를 등록하는 대행사 사용자 엔티티
     * @return 생성된 포트폴리오 응답 DTO
     */
    @Transactional
    public PortfolioResponse createPortfolio(PortfolioCreationRequest request, User agency) {
        Portfolio newPortfolio = Portfolio.builder()
                .agency(agency)
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUrl(request.getFileUrl()) // imageUrl 대신 fileUrl 사용
                .status(PortfolioStatus.PENDING_APPROVAL) // 초기 상태는 승인 대기 중
                .build();
        return PortfolioResponse.from(portfolioRepository.save(newPortfolio));
    }

    /**
     * 특정 대행사가 등록한 모든 포트폴리오 목록을 조회합니다.
     * 
     * @param agency 포트폴리오를 조회할 대행사 사용자 엔티티
     * @return 해당 대행사의 포트폴리오 목록
     */
    public List<PortfolioResponse> getPortfoliosByAgency(User agency) {
        return portfolioRepository.findByAgency(agency).stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 대행사가 자신의 포트폴리오를 수정합니다.
     * 
     * @param portfolioId 수정할 포트폴리오의 ID
     * @param request     포트폴리오 수정 요청 DTO
     * @param agency      현재 로그인된 대행사 사용자 엔티티 (소유자 확인용)
     * @return 수정된 포트폴리오 응답 DTO
     * @throws ResponseStatusException 포트폴리오를 찾을 수 없거나, 수정 권한이 없거나, 수정할 수 없는 상태인 경우
     *                                 발생
     */
    @Transactional
    public PortfolioResponse updatePortfolio(Long portfolioId, PortfolioUpdateRequest request, User agency) {
        // 1. 포트폴리오 조회 및 소유자 확인
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "포트폴리오를 찾을 수 없습니다."));

        if (!portfolio.getAgency().getId().equals(agency.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 포트폴리오에 대한 수정 권한이 없습니다.");
        }

        // 2. 포트폴리오 상태 확인 (APPROVED 상태일 경우 수정 불가, PENDING_APPROVAL/REJECTED 상태만 수정 가능)
        if (portfolio.getStatus().equals(PortfolioStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 승인된 포트폴리오는 수정할 수 없습니다.");
        }

        // 3. 필드 업데이트
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            portfolio.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            portfolio.setDescription(request.getDescription());
        }
        if (request.getFileUrl() != null && !request.getFileUrl().isEmpty()) { // fileUrl 대신 imageUrl 사용
            portfolio.setFileUrl(request.getFileUrl());
        }
        // 상태 변경 요청이 있다면 처리 (단, PENDING_APPROVAL 또는 REJECTED 상태에서만 가능하도록)
        if (request.getStatus() != null && !request.getStatus().equals(portfolio.getStatus())) {
            if (portfolio.getStatus().equals(PortfolioStatus.APPROVED)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "승인된 포트폴리오의 상태는 변경할 수 없습니다.");
            }
            portfolio.setStatus(request.getStatus());
        }

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    /**
     * 특정 ID로 포트폴리오를 조회합니다.
     * 
     * @param portfolioId 조회할 포트폴리오의 ID
     * @return 조회된 포트폴리오 응답 DTO
     */
    public PortfolioResponse getPortfolioById(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "포트폴리오를 찾을 수 없습니다."));
        return PortfolioResponse.from(portfolio);
    }

    /**
     * 특정 포트폴리오를 삭제합니다.
     * 
     * @param portfolioId 삭제할 포트폴리오의 ID
     * @param agency      삭제를 요청하는 대행사 사용자 엔티티 (소유자 확인용)
     * @throws ResponseStatusException 포트폴리오를 찾을 수 없거나, 삭제 권한이 없는 경우
     */
    @Transactional
    public void deletePortfolio(Long portfolioId, User agency) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "포트폴리오를 찾을 수 없습니다."));

        if (!portfolio.getAgency().getId().equals(agency.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 포트폴리오에 대한 삭제 권한이 없습니다.");
        }

        portfolioRepository.delete(portfolio);
    }
}