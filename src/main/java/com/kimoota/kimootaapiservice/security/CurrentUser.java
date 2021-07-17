package com.kimoota.kimootaapiservice.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;


// 현재 인증된 사용자의 principald를 컨트롤러에 삽입하는 데 사용 할 수 있는 메타 어노테이션

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
