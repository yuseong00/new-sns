package com.example.newsns.controller.response;

import com.example.newsns.model.AlarmArgs;
import com.example.newsns.model.AlarmDto;
import com.example.newsns.model.AlarmType;
import com.example.newsns.model.UserDto;
import com.example.newsns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {

    private Integer id;
//    private UserResponse userResponse; 유저정보는 필요가 없다.
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text; //문구가 필요하다.


    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;


    public static AlarmResponse fromAlarmDTO(AlarmDto alarmDto) {
        return new AlarmResponse(
                alarmDto.getId(),
                alarmDto.getAlarmType(),
                alarmDto.getAlarmArgs(),
                alarmDto.getAlarmType().getAlarmText(),
                alarmDto.getRegisteredAt(),
                alarmDto.getUpdatedAt(),
                alarmDto.getRemovedAt()
        );
    }
}
