package com.kimoota.kimootaapiservice.controller;

import com.kimoota.kimootaapiservice.domain.AuthProvider;
import com.kimoota.kimootaapiservice.domain.User;
import com.kimoota.kimootaapiservice.exception.BadRequestException;
import com.kimoota.kimootaapiservice.param.LoginParam;
import com.kimoota.kimootaapiservice.param.SignUpParam;
import com.kimoota.kimootaapiservice.repository.user.UserRepository;
import com.kimoota.kimootaapiservice.response.AuthResponse;
import com.kimoota.kimootaapiservice.service.oauth2.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

// 로그인 및 회원가입 컨트롤러
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProviderService tokenProviderService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginParam loginParam) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginParam.getEmail(),
                        loginParam.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProviderService.createTokenFrom(authentication);

        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpParam signUpParam) {

        if (userRepository.findByEmail(signUpParam.getEmail()).isPresent())
            throw new BadRequestException("email already used");

        User user = User.builder()
                .name(signUpParam.getName())
                .email(signUpParam.getEmail())
                .password(passwordEncoder.encode(signUpParam.getPassword()))
                .provider(AuthProvider.local)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
