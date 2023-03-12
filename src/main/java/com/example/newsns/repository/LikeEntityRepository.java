package com.example.newsns.repository;

import com.example.newsns.model.PostDto;
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

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity,Integer> {

    //클릭했는지에 대한 정보가 있어야한다.
    //유저와 포스트를 다 가지고 와서 이 유저가 이 포스트에 라이크버튼을 누른 로그가 있는지 확인한다.

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity postEntity);


    //    @Query(value = "SELECT COUNT(*) FROM LikeEntity  entity WHERE entity.post=:post" )
    //@Param("post")는 쿼리에서 사용되는 파라미터 이름과 매핑되는 이름을 명시해주는 것
    //  :post는 쿼리 내에서 해당 파라미터를 참조할 때 사용되는 이름입니다. 따라서 둘은 같은 이름으로 매핑되
//    Integer countByPost(@Param("post")PostEntity post);
    long countByPost(PostEntity postEntity);
    //count** 이 JPA에서 제공해주는 매서드이다.


    List<LikeEntity> findAllByPost(PostEntity postEntity);

    @Modifying //은 데이터베이스에서 데이터를 변경하는 작업을 수행하는 쿼리 메서드에 추가
    @Query("UPDATE LikeEntity entity SET removed_at =NOW() where entity.post=:post")
    void deleteAllByPost(PostEntity postEntity);
}
