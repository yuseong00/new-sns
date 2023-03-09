package com.example.newsns.controller.response;

import com.example.newsns.model.PostDto;
import com.example.newsns.model.UserDto;
import com.example.newsns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
@Getter
@AllArgsConstructor

public class PostResponse {


        private Integer id = null;

        private String title;

        private String body;

        private UserResponse user;

        private Timestamp registeredAt;

        private Timestamp updatedAt;

        private Timestamp removedAt;

        public static PostResponse fromEntity(PostDto postDto) {
                return new PostResponse(
                        postDto.getId(),
                        postDto.getTitle(),
                        postDto.getBody(),
                        UserResponse.fromUser(postDto.getUser()),
                        postDto.getRegisteredAt(),
                        postDto.getUpdatedAt(),
                        postDto.getRemovedAt()
                );

        }
}
