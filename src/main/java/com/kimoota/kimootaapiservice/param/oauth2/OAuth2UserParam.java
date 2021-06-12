package com.kimoota.kimootaapiservice.param.oauth2;

import lombok.Getter;

import java.util.Map;

// 사용자 데이터 파라매터
@Getter
public abstract class OAuth2UserParam {

    protected Map<String, Object> attributes;

    public OAuth2UserParam(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();


}
