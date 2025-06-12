package com.auction.adauctionbackend.proposal.dto;

import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.proposal.domain.Proposal;
import com.auction.adauctionbackend.proposal.domain.enums.ProposalStatus;
import com.auction.adauctionbackend.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponse {
    private Long id;
    private BidResponse bid;
    private UserResponse agency;
    private BigDecimal proposedPrice;
    private ProposalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProposalResponse from(Proposal proposal) {
        return ProposalResponse.builder()
                .id(proposal.getId())
                .bid(BidResponse.from(proposal.getBid()))
                .agency(UserResponse.from(proposal.getAgency()))
                .proposedPrice(proposal.getProposedPrice())
                .status(proposal.getStatus())
                .createdAt(proposal.getCreatedAt())
                .updatedAt(proposal.getUpdatedAt())
                .build();
    }
}