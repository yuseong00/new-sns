package com.example.newsns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {  //정상적인 로그인이 되었으면 토큰값 반환
    private String token;




}
