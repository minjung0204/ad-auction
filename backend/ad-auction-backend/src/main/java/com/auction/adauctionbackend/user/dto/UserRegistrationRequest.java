package com.auction.adauctionbackend.user.dto;

import com.auction.adauctionbackend.user.domain.enums.SocialType;
import com.auction.adauctionbackend.user.domain.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    private String password;

    private SocialType socialType;

    private String socialId;

    @NotNull(message = "사용자 유형은 필수입니다.")
    private UserType userType;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @NotBlank(message = "회사명은 필수입니다.")
    private String companyName;

    @NotBlank(message = "사업자등록번호는 필수입니다.")
    private String registrationNumber;

    private String accountNumber;

    private String bankName;
}