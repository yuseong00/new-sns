package com.example.newsns.configuration.filter;

import com.example.newsns.exception.CustomAuthenticationEntryPoint;
import com.example.newsns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().regexMatchers("^(?!/api/).*");
    }//^(?!/api/).*는 /api/로 시작하지 않는 모든 URL을 일치
    //이 코드는 /aprrri/로 시작하지 않는 모든 요청에 대해 인증 및 권한 부여 필터를 적용하지 않도록 Spring Security를 구성
    //HttpSecurity에 적용시킬수 있지만 따로 WebSecurity 성능최적화를 위해 따로 처리한다.
    //HttpSecurity 구성은 인증, 인가, csrf 및 세션 관리와 같은 보안 설정을 처리하므로 더 복잡한 로직을 처리하기에 적합합니다.
    // WebSecurity 구성은 간단한 정적 파일 또는 다른 경로의 미디어 파일과 같은 인증이 필요하지 않은 리소스를 처리하는 데 사용
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
             .antMatchers("/api/**").authenticated() //시큐리티 적용
             .and()
             .sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 적용은 안시킨다는 의미
             .and()
             //exceptionHandling 안해도 되지만 내가 원하는 예외처리응답을 보내주기위해서 쓴다.
             //인증이 실패한 경우에 처리할 AuthenticationEntryPoint 를 설정하는 메소드
             //CustomAuthenticationEntryPoint는 사용자가 인증되지 않은 경우에 호출
             .exceptionHandling() // 예외처리를 구성하는 메소드
             .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
     //필터를 하나 설정을 해서 매번 들어올때마다 토큰이 어떤유저를 가르키는지 체크하는 로직을 짠다.
             //addFilterBefore 의 첫번쨰 인자는 추가하고자 하는 필터 객체, 두번째는 첫번째를 두번쨰 클래스 앞에서 추가하겠다는 의미
             //UsernamePasswordAuthenticationFilter 요청이 들어오면  JwtTokenFilter 를 먼저 해준다는 의미
             // JwtTokenFilter 를 통해 토큰 유효성검증 처리가 이뤄지고
             // UsernamePasswordAuthenticationFilter username, password 에 인증 권한 부여한다.
             .and()
             .addFilterBefore(new JwtTokenFilter(userService,secretKey), UsernamePasswordAuthenticationFilter.class)
             .build();




 }
}
