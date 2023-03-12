package com.example.newsns.service;

import com.example.newsns.exception.ErrorCode;
import com.example.newsns.exception.SnsApplicationException;
import com.example.newsns.model.AlarmDto;
import com.example.newsns.model.UserDto;
import com.example.newsns.model.entity.UserEntity;
import com.example.newsns.repository.AlarmEntityRepository;
import com.example.newsns.repository.UserEntityRepository;
import com.example.newsns.utill.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    public final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    //@Value 는 스프링 프레임워크에서 제공하는 어노테이션 중 하나로,
    // 프로퍼티 파일에서 값을 읽어와서 expiredTimeMs 에 할당
    private Long expiredTimeMs;


    //원래는 extends해서 userDetailService 를 통해 사용자 인증정보를 갖고올수 있지만 커스텀마이징을 위해 따로 만듬.
    public UserDto loadUserByUserName(String userName){
        return userEntityRepository.findByUserName(userName).map(UserDto::fromEntity).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }


    @Transactional //Transactional 를 쓰면서 예외처리가 발생시 롤백이 되면서 저장이 안된다.
    public UserDto join(String userName, String password){
        //회원가입하려는 userName 으로 회원가입된 user 가 있는지 확인하고 없으면 에러처리

        userEntityRepository.findByUserName(userName).
                ifPresent(it->{    //it 는 userName 이다.
                    throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated"));});


        //회원가입 진행 = user 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));


        return UserDto.fromEntity(userEntity);
    }

    //로그인을 완료하였을 때 jwt 토큰으로 반환을 해야 하기에 String 으로 반환처리 해야 한다.
    public String login(String userName,String password){
        //회원가입이 안되어있으면 에러를 반환
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));

//       //비밀번호 맞는지 체크  (등록된 패스워드와 입력된 패스워드가 다른경우) //암호화된 패스워드가 아니다. 다시 체크해야한다.
//       if(!userEntity.getPassword().equals(password)){throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"");}

       //암호화된 패스워드로 매칭한다.
        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰 생성
        String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
        return token ; //사용자 이름과 비밀번호를 검증하고 로그인 성공시 토큰을을 생성
    }
    //로그인한 유저 정보, 페이징처리할 정보
    public Page<AlarmDto> alarmList(String userName, Pageable pageable) {
        //1)유저가 실제 존재한지 확인해야 한다.
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));

        //2)알람레포에 유저정보가 있는지 확인한다(like와 같이)

        //그리고 게시글을 작성,좋아요버튼을 하는 서비스로직시 그떄 alarmEntityRepository 에 저장하는 로직을 추가구현
        return alarmEntityRepository.findAllByUser(userEntity, pageable).map(AlarmDto::fromEntity);

    }
}
