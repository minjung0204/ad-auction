package com.auction.adauctionbackend.user.dto;

import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.domain.enums.SocialType;
import com.auction.adauctionbackend.user.domain.enums.UserStatus;
import com.auction.adauctionbackend.user.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private SocialType socialType;
    private String socialId;
    private UserType userType;
    private String name;
    private String phoneNumber;
    private String companyName;
    private String registrationNumber;
    private String accountNumber;
    private String bankName;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .socialType(user.getSocialType())
                .socialId(user.getSocialId())
                .userType(user.getUserType())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .companyName(user.getCompanyName())
                .registrationNumber(user.getRegistrationNumber())
                .accountNumber(user.getAccountNumber())
                .bankName(user.getBankName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}