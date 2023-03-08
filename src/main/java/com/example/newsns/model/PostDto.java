package com.example.newsns.model;

import com.example.newsns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
@Getter
@AllArgsConstructor
public class PostDto {

    private Integer id = null;

    private String title;

    private String body;

    private UserDto user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static PostDto fromEntity(PostEntity entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                UserDto.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
