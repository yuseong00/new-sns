package com.example.newsns.controller.response;

import com.example.newsns.model.CommentDto;
import com.example.newsns.model.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor

public class CommentResponse {


        private Integer id = null;

        private String comment;

        private String userName;

        private Integer postId;

        private Timestamp registeredAt;

        private Timestamp updatedAt;

        private Timestamp removedAt;

        public static CommentResponse fromCommentDto(CommentDto commentDto) {
                return new CommentResponse(
                        commentDto.getId(),
                        commentDto.getComment(),
                        commentDto.getUserName(),
                        commentDto.getPostId(),
                        commentDto.getRegisteredAt(),
                        commentDto.getUpdatedAt(),
                        commentDto.getRemovedAt()
                );

        }
}
