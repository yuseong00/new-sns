package com.example.newsns.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthenticationConfig {

 @Bean
    public SecurityFilterChain securityFilterChain(
         HttpSecurity http) throws Exception{
     return http.csrf().disable()
             .authorizeHttpRequests()
             .antMatchers("/api/*/users/join","/api/*users/login").permitAll() //시큐리티 미적용
             .antMatchers("/api/**").authenticated()
             .and()
             .sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 적용은 안시킨다는 의미
             .and()
             .build();



 }
}
