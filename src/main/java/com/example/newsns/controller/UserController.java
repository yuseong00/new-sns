package com.example.newsns.controller;

import com.example.newsns.controller.request.UserJoinRequest;
import com.example.newsns.controller.request.UserLoginRequest;
import com.example.newsns.controller.response.AlarmResponse;
import com.example.newsns.controller.response.Response;
import com.example.newsns.controller.response.UserJoinResponse;
import com.example.newsns.controller.response.UserLoginResponse;
import com.example.newsns.model.UserDto;
import com.example.newsns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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
        return Response.success(UserJoinResponse.fromUserDTO(userDTO));
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
    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication){
        //알람리스트를 페이징처리된형태로 해서 만든다.
        //1)alarmList 메소드를 통해 로그인유저정보를 확인하고
        //2)이를 map으로 RESPONSE로 반환

        //알람받는 유저의 정보를 통해 alarmEntity 의 alarmType,args 등의 정보를 entity->dto->response 객체변환하고 page으로 결과값반환
       return Response.success(userService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarmDTO));

    }




}
