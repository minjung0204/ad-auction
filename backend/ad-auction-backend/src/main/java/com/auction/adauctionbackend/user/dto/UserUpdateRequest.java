package com.auction.adauctionbackend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다.")
    private String phoneNumber;

    @NotBlank(message = "회사명은 필수입니다.")
    private String companyName;

    @NotBlank(message = "사업자등록번호는 필수입니다.")
    @Size(min = 10, max = 10, message = "사업자등록번호는 10자리여야 합니다.")
    private String registrationNumber;

    private String accountNumber;

    private String bankName;

    private String newPassword;

    private String currentPassword;
}