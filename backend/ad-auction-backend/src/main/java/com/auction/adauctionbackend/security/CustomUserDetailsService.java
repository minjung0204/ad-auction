package com.auction.adauctionbackend.security;

import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 주어진 사용자 이름(여기서는 이메일)으로 사용자 상세 정보를 로드합니다.
     * 
     * @param email 사용자의 이메일 주소
     * @return 로드된 UserDetails 객체
     * @throws UsernameNotFoundException 해당 이메일을 가진 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 사용자 권한을 SimpleGrantedAuthority로 변환합니다.
        // 여기서는 user.getUserType().name()을 사용하여 사용자 유형을 권한으로 설정합니다.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserType().name()) // 예: ADVERTISER, AGENCY, ADMIN
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}