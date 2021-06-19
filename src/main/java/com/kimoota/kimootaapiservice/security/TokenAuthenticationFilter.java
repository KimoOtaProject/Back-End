package com.kimoota.kimootaapiservice.security;


import com.kimoota.kimootaapiservice.service.oauth2.TokenProviderService;
import com.kimoota.kimootaapiservice.service.user.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProviderService tokenProviderService;
    private final CustomUserDetailService customUserDetailService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7, bearerToken.length());

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProviderService.validate(jwt)) {

                Long userId = tokenProviderService.getUserIdFrom(jwt);

                UserDetails userDetails = customUserDetailService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch (Exception e) {
            logger.error("error in setting user authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}
