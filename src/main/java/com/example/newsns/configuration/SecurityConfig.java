package com.example.newsns.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {


    @Bean //BCryptPasswordEncoder 는 security 메서드이다.
    public BCryptPasswordEncoder encoderPassword(){
        return new BCryptPasswordEncoder();
    }

}
