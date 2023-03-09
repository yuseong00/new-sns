package com.example.newsns.exception;

import com.example.newsns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.*;

@Slf4j
@RestControllerAdvice

public class GlobalControllerAdvice {

    /*  RestControllerAdvice 는 RestController 에서 발생하는 예외를 처리하는 메소드이다.
     *  GlobalControllerAdvice 클래스는 컨트롤러에서 발생하는 예외를 모두 처리할 수 있는 전역적인 예외 처리 클래스
     *  반복되는 예외 처리 코드를 각각의 컨트롤러에서 작성하지 않고, 하나의 클래스에서 모두 처리할 수 있게 하기 위해서
     *  GlobalControllerAdvice 를 한 이유이다.
     *
     */




    //ExceptionHandler 는 SnsApplicationException 발생했을 때 applicationHandler 메소드실행하게 한다.
    @ExceptionHandler(SnsApplicationException.class)
    //ResponseEntity 는 HTTP 응답의 엔티티를 나타내는 클래스 , 다양한 HTTP 응답 상태 코드, 응답 본문, 응답 헤더 및 기타 메타 데이터를 포함
    public ResponseEntity<?>applicationHandler(SnsApplicationException e){
        log.error("Error occurs{}",e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                //ErrorCode 에서 HttpStatus Enum 타입을 가지며, HttpStatus Enum 타입은 HTTP 응답 상태 코드를 반환
                .body(Response.error(e.getErrorCode().name()));
        //e.getErrorCode().name()은 ErrorCode Enum 객체의 이름을 반환하는 메소드입니다.
        //ErrorCode Enum 객체 DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"user name is duplicated")이거이다.
    }
        // 이때, name() 메소드를 사용하여 해당 Enum 상수의 이름을 문자열 형태로 반환합니다.
        // 이렇게 반환된 문자열은 ResponseEntity의 body 메소드에 전달되어 HTTP 응답 본문으로 사용
        // 따라서, SnsApplicationException이 발생하면 Spring Framework는
        // 자동으로 SnsErrorCode Enum 타입의 값을 문자열로 변환하여 HTTP 응답 본문에 사용할 수 있게 됩니다.


    @ExceptionHandler(SnsApplicationException.class)
    //ResponseEntity 는 HTTP 응답의 엔티티를 나타내는 클래스 , 다양한 HTTP 응답 상태 코드, 응답 본문, 응답 헤더 및 기타 메타 데이터를 포함
    public ResponseEntity<?>applicationHandler(RuntimeException e){
        log.error("Error occurs{}",e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INVALID_PERMISSION.name()));
    }
}