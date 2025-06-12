package com.auction.adauctionbackend.agency.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioCreationRequest {
    @NotBlank(message = "포트폴리오 제목은 필수입니다.")
    private String title;

    private String description;

    @NotBlank(message = "파일 URL은 필수입니다.")
    private String fileUrl;
}