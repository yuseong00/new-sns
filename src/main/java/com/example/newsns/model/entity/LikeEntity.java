package com.example.newsns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Table(name="like")
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE like SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL") //삭제가 안된 애들만 가지고 와야한다.
public class LikeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "title")
//    private String title;
//
//    @Column(name = "body",columnDefinition = "TEXT")
//    //Column 어노테이션은 필드에 칼럼 맾이정보를 설정할때 사용한다.
//    //columnDefinition 를 통해 body필드가 데이터베이스에서 TEXT 타입으로 매핑되도록 지정한다.
//    private String body;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;   //유저에 대한 정보가 필요하다. 좋아요버튼을 누를때 누가눌렀는지 필요

    @ManyToOne
    @JoinColumn(name="post_id")  //단방향관계이다 !! 반대쪽은 설정하지 않는다.
    private PostEntity post;  //어떤 게시글에 버튼을 눌렀는지 필요하다.

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


    //엔티티를 만들어주는 메서드이다.
    public static LikeEntity of(PostEntity postEntity, UserEntity userEntity) {
        LikeEntity entity = new LikeEntity();
        entity.setPost(postEntity);
        entity.setUser(userEntity);
        return entity;
    }
}
