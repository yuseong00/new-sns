package com.example.newsns.utill;

import java.util.Optional;

public class ClassUtils {


    public static <T> Optional<T> getSafeCastInstance(Object o, Class<T> clazz ){
        return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
    }
    //이 메서드를 사용하면, 객체를 안전하게 타입 변환하고, 타입 변환이 실패한 경우에는
    // Optional.empty()를 반환하여 NullPointerException과 같은 예외를 방지할 수 있습니다.
}
