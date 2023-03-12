package com.example.newsns.repository;

import com.example.newsns.model.entity.CommentEntity;
import com.example.newsns.model.entity.LikeEntity;
import com.example.newsns.model.entity.PostEntity;
import com.example.newsns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity,Integer> {

    //CommentEntity 엔티티의 post 필드가 주어진 PostEntity 객체와 일치하는
    //모든 CommentEntity 인스턴스를 데이터베이스에서 검색, page 형태로 반환
    //manytoone의 관계이기도 하고 하나의 게시글에 다중의 댓글이 달려있는걸 감안해서 생각해봐!!
    //한마디로 postentity에서 postId를 통해 그에 따른 comment 정보를 페이지반환
    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);

    @Transactional  //직접 쿼리를 작성해서 쓰는게 낫다.
    @Modifying //은 데이터베이스에서 데이터를 변경하는 작업을 수행하는 쿼리 메서드에 추가
    @Query("UPDATE CommentEntity entity SET romoved_at = NOW() where entity.post=:post")
    void deleteAllByPost(@Param("post")PostEntity postEntity);

//    void deleteAllByPost(PostEntity postEntity); jpa가 제공하는거 안쓰는게 낫다.
}
