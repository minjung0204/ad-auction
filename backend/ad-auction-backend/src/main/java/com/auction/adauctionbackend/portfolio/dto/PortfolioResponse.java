package com.auction.adauctionbackend.portfolio.dto;

import com.auction.adauctionbackend.portfolio.domain.Portfolio;
import com.auction.adauctionbackend.portfolio.domain.enums.PortfolioStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {
    private Long id;
    private Long agencyId;
    private String title;
    private String description;
    private String fileUrl;
    private PortfolioStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PortfolioResponse from(Portfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .agencyId(portfolio.getAgency().getId())
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .fileUrl(portfolio.getFileUrl())
                .status(portfolio.getStatus())
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .build();
    }
}