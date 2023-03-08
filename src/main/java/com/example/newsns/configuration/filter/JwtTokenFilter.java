package com.example.newsns.configuration.filter;

import com.example.newsns.model.UserDto;
import com.example.newsns.service.UserService;
import com.example.newsns.utill.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    //OncePerRequestFilter 필터 객체 생성과 초기화를 단 한 번만 수행하므로, 성능상 이점
    private final UserService userService;
    private final String key; // 키값은 따로 넣어준다.



    //메소드는 각각의 OncePerRequestFilter 에 의해 HTTP 요청에 대해 한 번 호출되며, 요청 객체와 응답 객체, 그리고 다음 필터 체인 객체를 매개변수로 받습니다
    //이 메소드의 구현체에서는 JWT 토큰의 유효성을 검사하는 작업을 수행
    //HttpServletRequest 객체에서 Authorization 헤더를 추출하여 JWT 토큰을 파싱하고, 토큰이 유효한지 검사합니다.
    // 토큰이 유효하지 않을 경우 인증 실패 처리를 수행하고, 그렇지 않은 경우 인증된 사용자 정보를 SecurityContext에 저장합니다.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //doFilterInternal 를 호출하게 되면  if문을 통해 토큰값 유무를 확인하고
    //확인된 정보(request,response)를 담아서  filterChain.doFilter 메소드를 통해 다음필터로 위임처리한다.
    //각 필터마다 역활이 다르니까 필터를 걸려내면서 원하는 정보를 받는다.

    //UserService에서 login 성공했을때 토큰값을 response 받았고, 그 토큰을 헤더에 넣었으며
    //요청이 올때마다 헤더의 토큰값을 확인하여 인증을 수행하게 된다.
    //그래서 JwtTokenUtils 에 claims 에 username을 넣어줫다.
    //claims 를 보고 username 을 꺼내줘서 username 가 실제 유효한지 검증하는 로직구현이다.

        //헤더를 먼저 꺼낸다. , HttpHeaders.AUTHORIZATION 인증헤더를 꺼낸다.
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        //jwt 토큰을 Bearer 토큰에 넣어주기에 이름이 "Bearer [토큰값]" 로 시작이 된다.
        if(header==null || header.startsWith("Bearer")){
            log.error("Error occurs while getting header");

            filterChain.doFilter(request,response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();
            //공백을 기준으로 bearer 와 토큰을 분리하여 토큰값만 불러온다.
            //토큰값만 꺼내와서 토큰값이 유효한지 확인하고 그리고 그 안에 만료기간,유저네임 유효체크


            //토큰값이 유효한지 체크는 만료기간 유효체크
            if(JwtTokenUtils.isExpired(token,key)){
                log.error("key is expired");
                filterChain.doFilter(request,response);
                return;

            }



            //토큰값만 안에 있는 userName 의 값 유효체크
            String userName = JwtTokenUtils.getUserName(token,key);     //토큰값을 파싱해서 userName을 받아와서
            UserDto userDto = userService.loadUserByUserName(userName); //기존 가입된 로그인의 USER정보를 받고
            //UsernamePasswordAuthenticationToken 는 Spring Security에서 사용되는 인증 객체
            //이 객체는 인증에 사용되는 주체(principal)와 자격 증명(credentials)을 저장
            //주체(principal)는 인증에 사용되는 사용자 정보를 나타내며, 보통 사용자 이름 또는 ID로 나타냅니다.
            // 자격 증명(credentials)은 주체(principal)가 제공한 암호화된 비밀번호나 인증 토큰 등을 나타냅니다
            //principal: (userDto) 인증 주체를 나타내는 객체 ,
            //credentials: 인증에 사용되는 자격 증명을 나타내는 객체,
            //authorities: 주체(principal)가 가지고 있는 권한 정보를 나타내는 객체, List는 가변이지만 of를 통해 권한정보들을 불변 객체로 만들어줘 스레드 안정성 보장
            //3개의 객체가 필요하다. 인증주체에게 자격증명,권한정보를 부여한다.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDto, null, userDto.getAuthorities());

            //setDetails 는 추가정보 설정시 사용 ,WebAuthenticationDetailsSource 를 통해 HTTP 요청과 관련된 정보를 제공하는데
            // 보통 buildDetails(request) 와 같이 사용한다.
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (RuntimeException e){
            log.error("Error occurs while validating {}",e.toString());
            filterChain.doFilter(request,response);
            return;
        }
        filterChain.doFilter(request,response);

    }
}

