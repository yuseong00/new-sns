package com.example.newsns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Table(name="comment", indexes = {
        @Index(name="post_id_idx",columnList ="post_id")
        //todo 위에 걸 써야 하는 이유?? PostEntityRepository 힌트
        //jpa관계나 무슨 관계에서는 조인문을 쓰게 되면 상대방의 정보를 조회해서 써야하는 경우가 생긴다.
        //CommentEntity 클래스 같은 경우도 user,post 와 조인하여 쿼리문을 짜야할텐가 있다.
        //인덱스를 지정함으로 보다 쉽게 찾을수가 있어 성능최적화를 위해 쓴다.
        //해당 이름은 보통 필드내에 관계되어있는 컬럼의 외래키이름과 동일시하여 코드를 명확하게 한다.
        //@Table(name="현재클래스이름", indexes = {
        //        @Index(name="찾고자하는 핃드의 외래키_idx",columnList ="찾고자하는 핃드의 외래키")
})
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE comment SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL") //삭제가 안된 애들만 가지고 와야한다.
public class CommentEntity {


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
    ///todo @JoinColumn은 다른 엔티티와의 관계를 정의하기 위한 어노테이션으로,
    // user_id 는 UserEntity 클래스의 식별자의 필드명으로 짓는다. user_id 는 식별자키를 나타낸것!!
    private UserEntity user;   //유저에 대한 정보가 필요하다. 좋아요버튼을 누를때 누가눌렀는지 필요

    @ManyToOne
    @JoinColumn(name="post_id")  //단방향관계이다 !! 반대쪽은 설정하지 않는다.
    private PostEntity post;  //어떤 게시글에 버튼을 눌렀는지 필요하다.

    @Column(name = "comment")
    private String comment;
    //todo 엔티티를 그대로 복사하되 필요한것들만 남기고 삭제한다.
    // 어떤 댓글을 썼다는 정보가 필요로 해서 해당 필드를 추가작업
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


    //엔티티를 만들어주는 팩토리 메서드이다.게시글 정보,유저정보,댓글내용을 포함하기 위해 모두포함하여 만듬
    //OF를 통해 만든 CommentEntity 객체는 해당 댓글이 어떤 게시글(PostEntity)에 작성되었는지(PostEntity),
    // 댓글을 작성한 유저(UserEntity) 정보와 댓글 내용(Comment)을 포함
    public static CommentEntity of(PostEntity postEntity, UserEntity userEntity ,String comment) {
        CommentEntity entity = new CommentEntity();
        entity.setPost(postEntity);
        entity.setUser(userEntity);
        entity.setComment(comment);
        return entity;
    }
}
