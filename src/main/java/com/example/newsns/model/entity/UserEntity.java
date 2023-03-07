package com.example.newsns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Table(name="user")
@Setter
@Entity
@SQLDelete(sql = "UPDATE user SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL") //삭제가 안된 애들만 가지고 와야한다.
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;   //유저의 권한 여부

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

    //of 메서드는 UserEntity 객체를 생성하는데 사용하는 팩토리 메서드이다.
    //of 메서드를 통해 userName, encodedPwd 정보를 담고 있는 새로운 UserEntity 객체를 담아 entity 생성
    public static UserEntity of(String userName, String encodedPwd) {
        UserEntity entity = new UserEntity();
        entity.setUserName(userName);
        entity.setPassword(encodedPwd);
        return entity;
    }

}
