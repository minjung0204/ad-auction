package com.auction.backend.portfolio.service;

import com.auction.backend.portfolio.domain.Portfolio;
import com.auction.backend.portfolio.repository.PortfolioRepository;
import com.auction.backend.portfolio.dto.PortfolioCreationRequest;
import com.auction.backend.portfolio.dto.PortfolioUpdateRequest;
import com.auction.backend.portfolio.dto.PortfolioResponse;
import com.auction.backend.user.domain.User;
import com.auction.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Transactional
    public PortfolioResponse createPortfolio(PortfolioCreationRequest request, Long agencyId) {
        User agency = userRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalArgumentException("Agency not found with id: " + agencyId));

        Portfolio portfolio = Portfolio.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUrl(request.getFileUrl())
                .agency(agency)
                .build();

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    @Transactional
    public PortfolioResponse updatePortfolio(Long portfolioId, PortfolioUpdateRequest request, Long agencyId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + portfolioId));

        if (!portfolio.getAgency().getId().equals(agencyId)) {
            throw new AccessDeniedException("You do not have permission to update this portfolio.");
        }

        portfolio.setTitle(request.getTitle());
        portfolio.setDescription(request.getDescription());
        portfolio.setFileUrl(request.getFileUrl());

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    @Transactional(readOnly = true)
    public List<PortfolioResponse> getPortfoliosByAgency(Long agencyId) {
        User agency = userRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalArgumentException("Agency not found with id: " + agencyId));
        return portfolioRepository.findByAgency(agency).stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioById(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + portfolioId));
        return PortfolioResponse.from(portfolio);
    }

    @Transactional
    public void deletePortfolio(Long portfolioId, Long agencyId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + portfolioId));

        if (!portfolio.getAgency().getId().equals(agencyId)) {
            throw new AccessDeniedException("You do not have permission to delete this portfolio.");
        }
        portfolioRepository.delete(portfolio);
    }
}