package com.example.newsns.model;

import com.example.newsns.model.entity.AlarmEntity;
import com.example.newsns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

@Slf4j
@Getter
@AllArgsConstructor
public class AlarmDto {

    private Integer id;
//    private UserDto user;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;

    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;


    public static AlarmDto fromEntity(AlarmEntity alarmEntity) {
        log.info("Call fromEntity");
        return new AlarmDto(
                alarmEntity.getId(),
//                UserDto.fromEntity(alarmEntity.getUser()),
                alarmEntity.getAlarmType(),
                alarmEntity.getArgs(),
                alarmEntity.getRegisteredAt(),
                alarmEntity.getUpdatedAt(),
                alarmEntity.getRemovedAt()
        );
    }
}
