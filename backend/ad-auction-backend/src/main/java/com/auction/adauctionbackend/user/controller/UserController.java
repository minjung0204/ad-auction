package com.auction.adauctionbackend.user.controller;

import com.auction.adauctionbackend.jwt.JwtTokenProvider;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.dto.AuthResponse;
import com.auction.adauctionbackend.user.dto.UserLoginRequest;
import com.auction.adauctionbackend.user.dto.UserRegistrationRequest;
import com.auction.adauctionbackend.user.dto.UserResponse;
import com.auction.adauctionbackend.user.dto.UserUpdateRequest;
import com.auction.adauctionbackend.user.repository.UserRepository;
import com.auction.adauctionbackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 회원가입을 처리하는 API 엔드포인트입니다.
     * 
     * @param request 회원가입 요청 DTO
     * @return 등록된 사용자 정보를 담은 응답 엔티티
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse registeredUser = UserResponse.from(userService.registerUser(request));
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * 로그인을 처리하는 API 엔드포인트입니다.
     * 로그인 성공 시 JWT 토큰을 발급합니다.
     * 
     * @param request 로그인 요청 DTO
     * @return 로그인 성공 시 JWT 토큰 및 사용자 정보를 담은 응답 엔티티
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody UserLoginRequest request) {
        // 1. AuthenticationManager를 통해 인증 시도 (UserService에서 처리)
        Authentication authentication = userService.loginUser(request);

        // 2. 인증 객체를 기반으로 Access Token 및 Refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // 3. 인증된 사용자 정보를 불러와 UserResponse 생성
        // Authentication.getName()은 CustomUserDetailsService에서 설정한 이메일입니다.
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자를 찾을 수 없습니다."));
        UserResponse userResponse = UserResponse.from(user);

        // 4. AuthResponse DTO에 담아 반환
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    /**
     * 현재 로그인된 사용자의 정보를 조회합니다.
     * 
     * @return 현재 로그인된 사용자 정보를 담은 응답 엔티티
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }

    /**
     * 현재 로그인된 사용자의 정보를 수정합니다.
     * 
     * @param request 사용자 정보 수정 요청 DTO
     * @return 수정된 사용자 정보를 담은 응답 엔티티
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest request) {
        User updatedUser = userService.updateUser(request);
        return ResponseEntity.ok(UserResponse.from(updatedUser));
    }
}