package com.kimoota.kimootaapiservice.param.oauth2;

import com.kimoota.kimootaapiservice.domain.AuthProvider;
import com.kimoota.kimootaapiservice.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

// 사용자 파라매터 오브젝트 팩토리
public class OAuth2UserParamFactory {

    public static OAuth2UserParam getOAuth2UserParam(String registrationId, Map<String, Object> attributes) {

        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString()))
            return GoogleOAuth2UserParam.builder().attributes(attributes).build();

        throw new OAuth2AuthenticationProcessingException("아직 지원 되지 않는 공급자 입니다.");

    }

}
