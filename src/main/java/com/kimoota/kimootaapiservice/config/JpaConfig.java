package com.kimoota.kimootaapiservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // jpa에서 auditing 및 DB 자동 갱신을 할 수 있도록 허용
public class JpaConfig {
}
