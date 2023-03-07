package com.example.newsns.controller.response;

import com.example.newsns.model.UserDTO;
import com.example.newsns.model.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    //반환할 정보만 변수로 담는다.
    private Integer id;
    private String  Name;


    public static UserJoinResponse fromUser(UserDTO user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUserName()
        );
    }

}
