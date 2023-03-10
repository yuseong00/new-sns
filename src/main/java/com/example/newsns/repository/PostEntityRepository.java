package com.example.newsns.repository;

import com.example.newsns.model.entity.PostEntity;
import com.example.newsns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity,Integer> {


    Page<PostEntity> findAllByUser(UserEntity userEntity, Pageable pageable);

    //성능최적화
    //메소드중에 findAllById 같은 경우는 셀렉트할때 문제가 되지 않는다. id같은 경우는 pk이기 때문에
    //항상 인덱스가 걸려있다. 성능상 문제는 없다.
    //그러면 유저가 작성 post를 전부다 갖고 오는데 인덱스가 안걸려있다.그렇기에 굉장히 늦은 속도를 갖게된다.
    //영어사전처럼 index(=색인)를 보게되면 앞에서 대략 위치가 어디있는지를 알려주는 역할을한다.
    //findAllByUser 같은경우도 셀렉트가 걸리게되면 userId로 바뀐다.
    //userId로 인덱스가 필요로 하다.
}
