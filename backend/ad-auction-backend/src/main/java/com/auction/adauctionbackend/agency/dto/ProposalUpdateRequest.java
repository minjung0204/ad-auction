package com.auction.adauctionbackend.agency.dto;

import com.auction.adauctionbackend.proposal.domain.enums.ProposalStatus;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalUpdateRequest {
    @DecimalMin(value = "0.01", message = "제안 가격은 0보다 커야 합니다.")
    private BigDecimal proposedPrice;
    private ProposalStatus status;
}