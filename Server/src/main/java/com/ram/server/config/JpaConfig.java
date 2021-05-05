package com.ram.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration//설정 파일에 대한 내용
@EnableJpaAuditing//jpa에 대한 감시를 활성화 시키겠다.
public class JpaConfig {
}
