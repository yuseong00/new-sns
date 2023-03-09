package com.example.newsns.controller;

import com.example.newsns.controller.request.UserJoinRequest;
import com.example.newsns.controller.request.UserLoginRequest;
import com.example.newsns.controller.response.Response;
import com.example.newsns.controller.response.UserJoinResponse;
import com.example.newsns.controller.response.UserLoginResponse;
import com.example.newsns.model.UserDto;
import com.example.newsns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //로그인시 UserJoinRequest 에 정보를 담는다.
    @PostMapping("/join") //회원가입
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        UserDto userDTO=userService.join(request.getName(), request.getPassword());
//        UserJoinResponse response = UserJoinResponse.fromUser(user);
//        //바로 response 로 반환할 수 있지만 실패할 경우가 있다.
        return Response.success(UserJoinResponse.fromUser(userDTO));
        //Response 에서 성공로직이랑 실패로직을 만들어서 반환한다.
        //성공했을떄,실패했을때의 반환값이 제각각이다.api 가져다 쓰는 프론트엔드쪽에서 쓰기 힘들다.
        //획일화된 응답형태가 필요하고 Response 클래스에서 그 구현을 한다.
    }

    @PostMapping("/login")  //로그인
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token =userService.login(request.getName(),request.getPassword());
        //Response은 응답을 처리하는 DTO클래스 이다.
        return Response.success(new UserLoginResponse(token));
    }


}
