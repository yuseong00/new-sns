package com.example.newsns.repository;

import com.example.newsns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity >findByUserName(String userName);

    //Optional 없을수도 있으니 이렇게 처리 한다.
    //JpaRepository 여기에는 name으로 찾는 메소드가 없어 findByUserName 새로 작성
}
