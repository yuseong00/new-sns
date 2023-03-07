package com.example.newsns.service;

import com.example.newsns.exception.SnsApplicationException;
import com.example.newsns.fixture.UserEntityFixture;
import com.example.newsns.model.entity.UserEntity;
import com.example.newsns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;
    @MockBean
    private BCryptPasswordEncoder encoder



    @Test
    public void  회원가입이_정상적으로_동작()throws Exception{
        String userName = "userName";
        String password=   "password";
        //회원가입시 userName 이 없는지 확인
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");

        //save 를 하면 저장한 엔티티를 반환해야 하는데 UserEntity 타입으로 목킹하여 optional 로 반환한다.
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));

        Assertions.assertDoesNotThrow(()->userService.join(userName,password));

    }

    @Test
    public void  회원가입시_userName_회원가입한_유저가_있는경우()throws Exception{
        String userName = "userName";
        String password=   "password";

        //UserEntityFixture 에 가상의 회원가입 내역을 만든다.
        UserEntity fixture = UserEntityFixture.get(userName, password);

        //회원가입시 userName 으로 찾았는데 가입내역이 있다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        //save 를 하면 저장한 엔티티를 반환해야 하는데 UserEntity 타입으로 목킹하여 optional 로 반환한다.
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        //가입내역이 있으면 에러처리는 진행한다.
        assertThrows(SnsApplicationException.class,()->userService.join(userName,password));

    }


    @Test
    public void 로그인이_정상적으로_동작()throws Exception{
        String userName = "userName";
        String password=   "password";


        //UserEntityFixture 에 가상의 회원가입 내역을 만든다.
        UserEntity fixture = UserEntityFixture.get(userName, password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(()->userService.login(userName,password));

    }

    @Test
    public void  로그인시_userName_회원가입한_유저가_없는경우()throws Exception{
        String userName = "userName";
        String password=   "password";

        //회원가입시 userName 으로 찾았는데 가입내역이 있다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        //가입내역이 있으면 에러처리는 진행한다.
        assertThrows(SnsApplicationException.class,()->userService.login(userName,password));

    }


    @Test
    public void  로그인시_password_틀린경우()throws Exception{
        String userName = "userName";
        String password=   "password";
        String wrongPassword= "password";


        //UserEntityFixture 에 가상의 회원가입 내역을 만든다.
        UserEntity fixture = UserEntityFixture.get(userName, password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        //가입내역이 있으면 에러처리는 진행한다.
        assertThrows(SnsApplicationException.class,()->userService.login(userName,wrongPassword));

    }

}
