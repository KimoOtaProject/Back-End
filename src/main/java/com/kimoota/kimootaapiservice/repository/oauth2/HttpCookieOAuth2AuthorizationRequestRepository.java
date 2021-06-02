package com.kimoota.kimootaapiservice.repository.oauth2;

import com.kimoota.kimootaapiservice.utils.CookieUtils;
import com.kimoota.kimootaapiservice.utils.SerializeUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    private static final int cookieExpireSec = 180; // 3분


    // 제공자에게 보낸 인증 요청 쿠키 조회
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {


        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie ->
                        SerializeUtils.deserialize(cookie.getValue(), OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    // 리스폰스에 인증 요청을 쿠키로써 저장
    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional.ofNullable(authorizationRequest)
                .ifPresentOrElse(authRequest -> {

                    CookieUtils.addCookie(
                            response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                            SerializeUtils.serialize(authRequest), cookieExpireSec);

                    String redirectUri = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

                    if (StringUtils.isNotBlank(redirectUri))
                        CookieUtils.addCookie(
                                response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri, cookieExpireSec);


                }, () -> {
                    CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
                    CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
                });
    }

    // deprecated
    // request, response 와 연관된 인증 요청을 제거 및 반환
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    // request, response 와 연관된 인증 요청을 쿠키에서 제거
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
