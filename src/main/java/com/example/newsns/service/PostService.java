package com.example.newsns.service;

import com.example.newsns.exception.ErrorCode;
import com.example.newsns.exception.SnsApplicationException;
import com.example.newsns.model.entity.PostEntity;
import com.example.newsns.model.entity.UserEntity;
import com.example.newsns.repository.PostEntityRepository;
import com.example.newsns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional//포스트 작성하는 메서드
    public void create(String title, String body, String userName) {
    //user find
        UserEntity userEntity =userEntityRepository
                .findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));
    //post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));

        //return




    }

}
