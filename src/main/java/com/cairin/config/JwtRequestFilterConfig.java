package com.cairin.config;

import com.cairin.filter.JwtRequestFilter;
import com.cairin.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtRequestFilterConfig {

    private final JwtUtil jwtUtil;

    public JwtRequestFilterConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil);
    }
}
