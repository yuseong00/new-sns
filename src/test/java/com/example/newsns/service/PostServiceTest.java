package com.example.newsns.service;

import com.example.newsns.exception.ErrorCode;
import com.example.newsns.exception.SnsApplicationException;
import com.example.newsns.fixture.PostEntityFixture;
import com.example.newsns.fixture.TestInfoFixture;
import com.example.newsns.fixture.UserEntityFixture;
import com.example.newsns.model.entity.PostEntity;
import com.example.newsns.model.entity.UserEntity;
import com.example.newsns.repository.PostEntityRepository;
import com.example.newsns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private  PostService postService;

    @MockBean private PostEntityRepository postEntityRepository;
    //@MockBean 어노테이션은 외부 의존성을 가진 빈 객체를 가짜객체로 대체하는데 사용한다.


    @MockBean private UserEntityRepository userEntityRepository;


    @Test
    void 포스트작성이_성공한경우   ()throws Exception{

        String title ="title";
        String body="body";
        String userName="userName";

        //mocking
        //UserEntity.class 의 가짜 객체를 만들기 위해 mock 를 사용하고
        // findByUserName 메소드 반환타입이 Optional 이여서 Optional.of 사용하여 객체를 감싸줘야 한다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        //save 메소드는 void 반환이라서 특별히 객체를 감싸줄 필요는 없다.
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));


       Assertions.assertDoesNotThrow(()-> postService.create(title,body,userName));

    }

    @Test
    void 포스트작성시_요청한유저가_존재하지않은경우   ()throws Exception{

        String title ="title";
        String body="body";
        String userName="userName";

        //mocking
        //UserEntity.class 의 가짜 객체를 만들기 위해 mock 를 사용하고
        // findByUserName 메소드 반환타입이 Optional 이여서 Optional.of 사용하여 객체를 감싸줘야 한다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        //save 메소드는 void 반환이라서 특별히 객체를 감싸줄 필요는 없다.
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        //assertThrows 는 예외처리를 검증하는 메소드로서 a를 통해 검증할 예외처리타입, b는 예외처리를 하기 위해 실행할 람다식
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND,e.getErrorCode());

    }

    @Test
    void 포스트수정이_성공한경우   ()throws Exception{

        String title ="title";
        String body="body";
        String userName="userName";
        Integer postId=1;


        //mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        //save 메소드는 void 반환이라서 특별히 객체를 감싸줄 필요는 없다.
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));


        Assertions.assertDoesNotThrow(()-> postService.modify(title,body,userName,postId));
    }
    @Test
    void 포스트수정이_포스트가존재하지_않는경우   ()throws Exception{

        String title ="title";
        String body="body";
        String userName="userName";
        Integer postId=1;


        //mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        //Optional.empty 포스트가 존재하지 않는 경우
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName,postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND,e.getErrorCode());

    }

    @Test
    void 포스트수정이_권한이_없는경우   ()throws Exception{

        String title ="title";
        String body="body";
        String userName="userName";
        Integer postId=1;


        //mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity writer = UserEntityFixture.get("userName1","password",2)


        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName,postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION,e.getErrorCode());

    }



    @Test
    void 포스트생성시_유저가_존재하지_않으면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


}
