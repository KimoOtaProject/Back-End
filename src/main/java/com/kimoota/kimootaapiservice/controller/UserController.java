package com.kimoota.kimootaapiservice.controller;

import com.kimoota.kimootaapiservice.domain.User;
import com.kimoota.kimootaapiservice.exception.ResourceNotFoundException;
import com.kimoota.kimootaapiservice.repository.user.UserRepository;
import com.kimoota.kimootaapiservice.security.CurrentUser;
import com.kimoota.kimootaapiservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 현재 인증된 사용자의 세부 정보를 가져 오는 유저 컨트롤러
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
