package com.example.newsns.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {


    //알람을 발생시킨 사람(댓글을 단사람의 유저, 좋아요를 한 유저)
    private Integer fromUserId;

    //알람을 받는 사람
    private Integer targetId;

}
