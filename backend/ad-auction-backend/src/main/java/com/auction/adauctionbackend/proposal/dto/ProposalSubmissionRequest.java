package com.auction.adauctionbackend.proposal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalSubmissionRequest {
    @NotNull(message = "제안 가격은 필수입니다.")
    private BigDecimal proposedPrice;

    private String comments;
}