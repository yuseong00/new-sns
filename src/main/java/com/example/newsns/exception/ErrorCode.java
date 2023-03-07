package com.example.newsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"user name is duplicated"),

    INTERVAL_SERVER_ERROR(HttpStatus.CONFLICT,"interval server error")
    ;


    private HttpStatus status;
    private String message;
}
