package com.example.newsns.controller.response;

import com.example.newsns.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public
class UserResponse {
    private Integer id;
    private String userName;

    public static UserResponse fromUser(UserDto user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
