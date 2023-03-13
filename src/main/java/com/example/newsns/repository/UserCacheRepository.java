package com.example.newsns.repository;

import com.example.newsns.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    //이 클래스의 역할은 redis에다가 user를 캐싱하고 그 캐시해서 다시 user로 가져온다.

    private final RedisTemplate<String, UserDto> userDtoRedisTemplate;
    /*RedisTemplate 는 Redis를 사용하기 위해 Spring Data Redis에서 제공하는 RedisTemplate이다.
     이 RedisTemplate은 Redis에 접근하기 위한 다양한 Redis 연산을 제공하고, Redis에 데이터를 저장하거나
     가져올 때 유용한 메서드를 제공, 특히나 opsForValue 메서드는 Redis 문자열 값을 가져오거나 추가하거나 업데이트하거나 삭제할 때 사용
    */

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);
    //만료시간을 설정을 줘 일정시간동안 서비스를 사용하면 캐싱을 통해 내용전달,그렇지만 미사용시 불필요한 부하이기에 만료시간을 설정

    //user 정보를 인자로 받아 key, value값으로 세팅을 해주는 메소드
    public void setUser (UserDto userDto){
        String key = getKey(userDto.getUsername());
        log.info("Set User to Redis {}({})", key, userDto);
        userDtoRedisTemplate.opsForValue().set(getKey(userDto.getUsername()),userDto);
    }



    //키값을 인자로 입력시 value값을 가져오는 메소드
    //getUser 출력시 null 값이 있을수 있으니까 Optional 로  감싼다.
    public Optional<UserDto>  getUser(String userName ) {
        UserDto data = userDtoRedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get User from Redis {}", data);
        return Optional.ofNullable(data);
    }


    //key 값을 정의하는 메서드
    /*key 값을 어떤걸로 정의해야 할까?
       redis 에서 데이터를 캐싱하는 목적은 매번 데이터를 DB에서 가져오는 것보다 더 빠르게 데이터를 검색하기 위함이다.
       filter 에서 username으로 체크하는 로직이 많으며, 게시글,like 등 username 유효 검증하는 로직이 많다.
       redis는 보통 하나의 cluster로 만들어두고 거기에다가 service에서 쓰이는 모든 캐싱을 다 넣게 된다.
       user,comment,like 의 데이터를 캐싱할 수 있다. 키값을 username 만 하면 어떤 데이터의 키값인지 알수가 없다.
       redis에 저장할때는 항상 prefix 를 사용하며 "USER"  를 통해 식별키의 출처를 확인한다.
     */
    private String getKey(String userName){
        return "USER:" + userName;
    }

}
