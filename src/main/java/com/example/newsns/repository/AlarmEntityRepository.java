package com.example.newsns.repository;

import com.example.newsns.model.entity.AlarmEntity;
import com.example.newsns.model.entity.LikeEntity;
import com.example.newsns.model.entity.PostEntity;
import com.example.newsns.model.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity,Integer> {

    //유저정보를 가져와야 한다.
    // UserEntity 객체를 매개변수로 받아서, 해당 유저와 조인된 AlarmEntity 객체들을 page 반환
//    Page<AlarmEntity> findAllByUser(UserEntity userEntity, Pageable pageable);

    //필요한 정보는 UserEntity 보다는 정확하게 userId
    Page<AlarmEntity> findAllByUserId(Integer userId, Pageable pageable);



}
