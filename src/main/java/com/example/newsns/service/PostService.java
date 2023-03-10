package com.example.newsns.service;

import com.example.newsns.exception.ErrorCode;
import com.example.newsns.exception.SnsApplicationException;
import com.example.newsns.model.PostDto;
import com.example.newsns.model.entity.LikeEntity;
import com.example.newsns.model.entity.PostEntity;
import com.example.newsns.model.entity.UserEntity;
import com.example.newsns.repository.LikeEntityRepository;
import com.example.newsns.repository.PostEntityRepository;
import com.example.newsns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    @Transactional//포스트 작성하는 메서드
    public void create(String title, String body, String userName) {
        //user find
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        //post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));

        //return


    }


    @Transactional
    public PostDto modify(String title, String body, String userName, Integer postId) {
        //1)유저의 정보가 있는지 유무 확인후 갖고온다.
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        //2)포스트의 정보가 있는지 유무 확인후 갖고온다.
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        //3)게시글을 쓴 유저정보와 회원가입된 유저정보와 같은지 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has nno permission with %s", userName, postId));

        }

        //게시글 저장
        postEntity.setTitle(title);
        postEntity.setBody(body);

        return PostDto.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        //1)유저의 정보가 있는지 유무 확인후 갖고온다.
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        //2)포스트의 정보가 있는지 유무 확인후 갖고온다.
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        //3)게시글을 쓴 유저정보와 회원가입된 유저정보와 같은지 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has nno permission with %s", userName, postId));

        }
        postEntityRepository.delete(postEntity);
    }

    public Page<PostDto> list(Pageable pageable) {

        //findAll 메소드는 이미 Pageable 과 연관되어있는 메소드이다.페이지 형태로 반환해준다.
        //findAll 메소드는 page<t>는 Pageable 인터페이스를 구현한 제네릭 클래스이다.
        //pageimpl 는 page 인터페이스를 구현하고 있다. 결국 pageimpl 에서 페이징 관련처리를 해주는 메소드가 있따.
        //map 메서드는 스트림 내 요소들을 하나씩 꺼내어 지정됨 함수를 적용하여 새로운 스트림으로 반환하는 기능
        // postEntityRepository.findAll(pageable)를 통해 PostEntity의 모든 엔티티 객체를 Page<PostEntity> 타입으로 반환
        //.map(PostDto::fromEntity) Page<PostEntity> 타입의 객체를 Page<PostDto> 타입의 객체로 변환
        //Page 타입은 그대로 유지되면서 Page의 객체 안에만 map을 통해 엔티티->dto로 반환된다.
        return postEntityRepository.findAll(pageable).map(PostDto::fromEntity);
    }

    public Page<PostDto> my(String userName, Pageable pageable) {
        //1)유저의 정보가 있는지 유무 확인후 갖고온다.
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        //2)유저가 작성한 리스트를 페이징해야한다~ findAllByUser 를 구현해줘야 한다.
        return postEntityRepository.findAllByUser(userEntity, pageable).map(PostDto::fromEntity);
    }

    //포스트 정보, 유저정보가 필요하다
    @Transactional
    public void like(Integer postId, String userName) {

        //1)유저의 정보가 있는지 유무 확인후 갖고온다.
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        //2)포스트의 정보가 있는지 유무 확인후 갖고온다.
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        //3)좋아요버튼을 한번만 누를수 있게한다. 그래서 눌렀는지데 대한 확인 유무처리를 해야한다.
        //1)LikeEntity 를 만든다. 2)LikeEntityRepository 를 만든다.
        //2) 만약 좋아요버튼을 누르면 postEntity의 postId에 유저정보가 포함되어있다. 그 유저정보의유무확인을 통해 에러처리진행
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED_POST, String.format("%s username already like post %d", userName, postId));});


        //4)유저정보 확인 유무 후 유저정보가 없으면 저장
        likeEntityRepository.save(LikeEntity.of(postEntity, userEntity));

        }


    @Transactional
    public int likeCount(Integer postId) {


        //1)포스트의 정보가 있는지 유무 확인후 갖고온다.
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

//        //postEntity 의 정보를 갖고오되 LikeEntity 의 필드에 있는정보에 한해서 자료를 반환한다.
//        List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);
//        //size를 쓰기위해 list로 반환했다.
//        return likeEntities.size();
        //위와같이 할수 있지만 쿼리를 직접짜서 메소드로 만들어서 할수도 있다.
        return likeEntityRepository.countByPost(postEntity);

    }
    }
