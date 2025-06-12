package com.auction.adauctionbackend.portfolio.dto;

import com.auction.adauctionbackend.portfolio.domain.enums.PortfolioStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioUpdateRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    private String fileUrl;
    private PortfolioStatus status;
}