package com.auction.adauctionbackend.user.service;

import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.domain.enums.UserStatus;
import com.auction.adauctionbackend.user.dto.UserLoginRequest;
import com.auction.adauctionbackend.user.dto.UserRegistrationRequest;
import com.auction.adauctionbackend.user.dto.UserUpdateRequest;
import com.auction.adauctionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * 새로운 사용자를 등록합니다. 이메일 또는 소셜 로그인을 통해 가입할 수 있습니다.
     * 
     * @param request 사용자 등록 요청 DTO
     * @return 등록된 사용자 엔티티
     * @throws IllegalArgumentException 이메일이 이미 존재하거나, 소셜 타입이 정의되지 않았을 경우 발생
     */
    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 소셜 로그인인 경우 비밀번호는 null 허용
        String encodedPassword = null;
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .socialType(request.getSocialType())
                .socialId(request.getSocialId())
                .userType(request.getUserType())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .registrationNumber(request.getRegistrationNumber())
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .status(UserStatus.ACTIVE) // 초기 상태는 ACTIVE
                .build();

        return userRepository.save(newUser);
    }

    /**
     * 사용자 로그인을 처리합니다.
     * 
     * @param request 사용자 로그인 요청 DTO
     * @return 로그인 성공 시 Authentication 객체
     * @throws IllegalArgumentException 이메일 또는 비밀번호가 일치하지 않을 경우 발생
     */
    public Authentication loginUser(UserLoginRequest request) {
        // 인증을 시도합니다.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // 인증 성공 시, SecurityContextHolder에 인증 객체를 설정하는 것은 필터에서 처리될 것이므로 여기서는 반환만 합니다.
        return authentication;
    }

    /**
     * 현재 인증된 사용자의 정보를 조회합니다.
     * 
     * @return 현재 로그인된 사용자 엔티티
     * @throws IllegalStateException 인증 정보가 SecurityContext에 없거나 사용자를 찾을 수 없을 때 발생
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증된 사용자를 찾을 수 없습니다.");
        }

        String username = authentication.getName(); // CustomUserDetailsService에서 설정한 이메일
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다: " + username));
    }

    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     * 
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 엔티티
     * @throws ResponseStatusException ID에 해당하는 사용자를 찾을 수 없을 때 발생
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다: " + id));
    }

    /**
     * 현재 로그인된 사용자의 정보를 수정합니다.
     * 
     * @param request 사용자 정보 수정 요청 DTO
     * @return 수정된 사용자 엔티티
     * @throws IllegalArgumentException 비밀번호가 일치하지 않거나, 현재 사용자를 찾을 수 없을 때 발생
     */
    @Transactional
    public User updateUser(UserUpdateRequest request) {
        User currentUser = getCurrentUser(); // 현재 로그인된 사용자 정보 가져오기

        // 비밀번호 변경 요청이 있을 경우
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isEmpty() ||
                    !passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        // 기타 정보 업데이트
        currentUser.setName(request.getName());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setCompanyName(request.getCompanyName());
        currentUser.setRegistrationNumber(request.getRegistrationNumber());
        currentUser.setAccountNumber(request.getAccountNumber());
        currentUser.setBankName(request.getBankName());

        return userRepository.save(currentUser);
    }
}