package com.example.newsns.model.entity;

import com.example.newsns.model.AlarmArgs;
import com.example.newsns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Table(name="alram", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//TODO 데이터 타입형식의 변화로 추가해줘야 한다.추가한 라이브러리에 JsonBinaryType 가 들어가있다.
@SQLDelete(sql = "UPDATE alram SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL") //삭제가 안된 애들만 가지고 와야한다.
public class AlarmEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //알람을 받는사람 !!
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;   //유저에 대한 정보가 필요하다. 알람을 받는 사람이 누군지필요

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType; //AlarmType 의 종류가 많을수 있기에 enum으로 정의

    @Type(type = "jsonb")
    // 해당 엔티티 필드가 JSONB 타입으로 데이터베이스에 저장될 것임을 지정
    //  PostgreSQL 는 JSONB, Json이 다 적용이 되는데 JSONB형식은 이진형태로 저장되어 보다 빠른검색,쿼리처리가 가능하다.
    // jSONB 타입으로 저장하여 성능이점을 취하고자 jSONB 데이터형식으로 저장
    // JSONB 를 PostgreSQL 만 있는 타입으로 라이브러리를 추가해야한다.
    @Column(columnDefinition = "json")
    //해당 엔티티 필드가 JSON 형식으로 데이터베이스에 저장될 것임을 나타내는 JPA 어노테이션
    private AlarmArgs args;

    @Column(name = "registered_at")
    private Timestamp registeredAt;//등록시간

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @PrePersist  //저장되기전에 자동으로 현재시간을 넣는다.
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate   //자동으로 업데이트 시 업데이트 시간저장
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }


    //엔티티를 만들어주는 팩토리메서드이다.
    public static AlarmEntity of(UserEntity userEntity,AlarmType alarmType, AlarmArgs args) {
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        entity.setArgs(args);
        return entity;
    }
}
