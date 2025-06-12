package com.auction.adauctionbackend.agency.dto;

import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.proposal.dto.ProposalResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgencyBidDetailsResponse {
    private BidResponse bid;
    private List<ProposalResponse> proposals;
}