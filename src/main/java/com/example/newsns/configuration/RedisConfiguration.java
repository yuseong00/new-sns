package com.example.newsns.configuration;

import com.example.newsns.model.UserDto;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfiguration {

    //설정에 관한 정보는 클래스에 직접 입력하지 않고 yaml 에 넣어준다.
    //결합성을 줄이기 위해 yaml에 넣고, 관리하기가 yaml에서  전체적으로 관리하는게 관리,수정이 용이하다.

    private final RedisProperties redisProperties;





    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
    RedisURI redisURI = RedisURI.create(redisProperties.getUrl());
    org.springframework.data.redis.connection.RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
    //Lettuce 를 통해 redis connection 을 해준다.
    LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
    //afterPropertiesSet 해줘야 지만 초기화(initializer) 할수 있다.
    factory.afterPropertiesSet();
    return factory;

    }




        @Bean
    //Redis 데이터에 접근하기 쉬게 해주는 코드 RedisTemplate 의 역할
    // 접근이 많은 데이터를 캐싱할수록 db에 대한 부담이 적어 그 데이터를 사용해야한다.
    // (KEY)-String (Value)-UserDto   , UserDto 데이터의 접근로직이 가장 많다.
    public RedisTemplate<String, UserDto> userRedisTemplate() {
        RedisTemplate<String, UserDto> redisTemplate = new RedisTemplate<>();
        // Redis 서버에 대한 정보가 있어야 정보전달이 가능하다.
        //서버에 대한 정보는 redisConnectionFactory 에 담겨있고 그 서버에 대한 정보를 연결해주는  setConnectionFactory 메서드
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        //데이터를 redis에 저장할때 Serializer 해서 넣어야 한다. setKeySerializer 가 그 역할을 해준다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value 같은 경우도 Serializer 해야 하며 value 클래스는 userDto를 사용했다.
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<UserDto>(UserDto.class));

        return redisTemplate;
    }

}
