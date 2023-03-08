package com.example.newsns.configuration.filter;

import com.example.newsns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserService userService;

    @Value("${jwt.secret-key}")  //다른 파일에 키값을 저장시켜놓는다.안정성,키 유출우려
    private String secretKey;

 @Bean
    public SecurityFilterChain securityFilterChain(
         HttpSecurity http) throws Exception{

     //csrf().disable() 코드는 Spring Security에서 제공하는 CSRF 보호 기능을 해제하는 설정입니다.
     //그렇지만 프론트엔드와 백엔드가 분리된 구조에서 , 다른 도메인에서 요청을 보내는경우 이를 해제설정을한다.
     //혹은 Ajax나 WebSocket 등에서 요청을 보내는 경우
     //RESTful API를 사용하여 다른 클라이언트에서 요청을 보내는 경우
     return http.csrf().disable()
             .authorizeHttpRequests()
             .antMatchers("/api/*/users/join","/api/*users/login").permitAll() //시큐리티 미적용
             .antMatchers("/api/**").authenticated()
             .and()
             .sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 적용은 안시킨다는 의미
             .and()
     //필터를 하나 설정을 해서 매번 들어올때마다 토큰이 어떤유저를 가르키는지 체크하는 로직을 짠다.
             //addFilterBefore 의 첫번쨰 인자는 추가하고자 하는 필터 객체, 두번째는 첫번째를 두번쨰 클래스 앞에서 추가하겠다는 의미
             //UsernamePasswordAuthenticationFilter 요청이 들어오면  JwtTokenFilter 를 먼저 해준다는 의미
             // JwtTokenFilter 를 통해 토큰 유효성검증 처리가 이뤄지고
             // UsernamePasswordAuthenticationFilter username, password 에 인증 권한 부여한다.
             .addFilterBefore(new JwtTokenFilter(userService,secretKey), UsernamePasswordAuthenticationFilter.class)
             .build();




 }
}
