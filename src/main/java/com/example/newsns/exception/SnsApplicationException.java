package com.example.newsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnsApplicationException extends  RuntimeException{

    private ErrorCode errorCode;
    private String message;

    //메시지 내역없이 에러처리만 날릴수 있으니까 메시지 없는 생성자를 만든다.
    public SnsApplicationException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = null;
    }

    //getMessage 메소드는 SnsApplicationException 가 생성자 호출하였을때 오버라이드를 거쳐서 정보를 반환한다.
    @Override
    public String getMessage() {
        if (message==null){
            return errorCode.getMessage();
        }

        return String.format("%s.%s", errorCode.getMessage(),message);
    }
}
