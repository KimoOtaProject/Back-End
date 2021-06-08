package com.kimoota.kimootaapiservice.service.user;

import com.kimoota.kimootaapiservice.domain.AuthProvider;
import com.kimoota.kimootaapiservice.domain.User;
import com.kimoota.kimootaapiservice.exception.OAuth2AuthenticationProcessingException;
import com.kimoota.kimootaapiservice.param.oauth2.OAuth2UserParam;
import com.kimoota.kimootaapiservice.param.oauth2.OAuth2UserParamFactory;
import com.kimoota.kimootaapiservice.repository.user.UserRepository;
import com.kimoota.kimootaapiservice.security.oauth2.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


// 시큐리티의 DefaultOAuth2UserService 를 상속 받는 유저 서비스
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService  extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return process(oAuth2UserRequest, oAuth2User);
        }
        catch (AuthenticationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private User create(OAuth2UserRequest oAuth2UserRequest, OAuth2UserParam oAuth2UserParam) {

        User user = User.builder()
                .provider(
                        AuthProvider.valueOf(
                                oAuth2UserRequest
                                        .getClientRegistration()
                                        .getRegistrationId()))
                .providerId(oAuth2UserParam.getId())
                .email(oAuth2UserParam.getEmail())
                .name(oAuth2UserParam.getName())
                .imageUrl(oAuth2UserParam.getImageUrl())
                .build();

        return userRepository.save(user);
    }

    private User update(User user, OAuth2UserParam oAuth2UserParam) {

        user.setName(oAuth2UserParam.getName());
        user.setImageUrl(oAuth2UserParam.getImageUrl());

        return userRepository.save(user);
    }

    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        OAuth2UserParam oAuth2UserParam = OAuth2UserParamFactory
                .getOAuth2UserParam(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (Optional.ofNullable(oAuth2UserParam.getEmail()).isEmpty())
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");

        Optional<User> optionalUser = userRepository.findByEmail(oAuth2UserParam.getEmail());
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if(!user.getProvider()
                    .equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())))
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getProvider() +
                                " account. Please use your " + user.getProvider() + " account to login.");

            user = update(user, oAuth2UserParam);
        }
        else
            user = create(oAuth2UserRequest, oAuth2UserParam);

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

}
