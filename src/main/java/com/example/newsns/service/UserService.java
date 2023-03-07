package com.example.newsns.service;

import com.example.newsns.exception.ErrorCode;
import com.example.newsns.exception.SnsApplicationException;
import com.example.newsns.model.UserDTO;
import com.example.newsns.model.entity.UserEntity;
import com.example.newsns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userEntityRepository;
    public final BCryptPasswordEncoder encoder;

    @Transactional //Transactional 를 쓰면서 예외처리가 발생시 롤백이 되면서 저장이 안된다.
    public UserDTO join(String userName, String password){
        //회원가입하려는 userName 으로 회원가입된 user 가 있는지 확인하고 없으면 에러처리

        userEntityRepository.findByUserName(userName).
                ifPresent(it->{    //it 는 userName 이다.
                    throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated"));});


        //회원가입 진행 = user 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));


        return UserDTO.fromEntity(userEntity);
    }

    //로그인을 완료하였을 때 jwt 토큰으로 반환을 해야 하기에 String 으로 반환처리 해야 한다.
    public String login(String userName,String password){
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).
                orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));


        //비밀번호 맞는지 체크  (등록된 패스워드와 입력된 패스워드가 다른경우)
        if(!userEntity.getPassword().equals(password)){
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"");
        }
        //토큰 생성

        return "";
    }
}
