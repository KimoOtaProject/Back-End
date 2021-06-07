package com.kimoota.kimootaapiservice.param.oauth2;


import lombok.Builder;

import java.util.Map;

public class GoogleOAuth2UserParam extends OAuth2UserParam {

    @Builder
    public GoogleOAuth2UserParam(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}