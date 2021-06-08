package com.kimoota.kimootaapiservice.handler.oauth2;

import com.kimoota.kimootaapiservice.config.AppProperties;
import com.kimoota.kimootaapiservice.exception.BadRequestException;
import com.kimoota.kimootaapiservice.repository.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.kimoota.kimootaapiservice.service.oauth2.TokenProviderService;
import com.kimoota.kimootaapiservice.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.kimoota.kimootaapiservice
        .repository
        .oauth2
        .HttpCookieOAuth2AuthorizationRequestRepository
        .REDIRECT_URI_PARAM_COOKIE_NAME;

// OAuth2 인증 성공 시 호출되는 핸들러 -> 성공 시 onAuthenticationSuccess 메소드 호출
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppProperties appProperties;
    private final TokenProviderService tokenProviderService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    // URI 검증
    private boolean isAuthorizedRedirectUri(String uri) {

        URI clientRedirectUri = URI.create(uri);

        return appProperties
                .getOAuth2()
                .getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {

                    // host & port 검증
                    URI authorizedURI = URI.create(authorizedRedirectUri);

                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && (authorizedURI.getPort() == clientRedirectUri.getPort());
                });
    }

    // redirect URI 반환
    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        Optional<String> redirectUri = CookieUtils
                .getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get()))
            throw new BadRequestException("unauthorized redirect URI");

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String token = tokenProviderService.createTokenFrom(authentication);

        return UriComponentsBuilder
                .fromUriString(targetUrl)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    // 리퀘스트 쿠키 클리어
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {

        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // OAuth2 인증 성공 시 호출
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed: " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
