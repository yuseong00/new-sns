package com.example.newsns.model;

import com.example.newsns.model.entity.CommentEntity;
import com.example.newsns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Integer id = null;

    private Integer postId ;

    private String comment;

    private String userName;


    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static CommentDto fromEntity(CommentEntity entity) {
        return new CommentDto(
                entity.getId(),
                entity.getPost().getId(),
                entity.getComment(),
                entity.getUser().getUserName(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
