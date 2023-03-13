package com.example.newsns.model;

import com.example.newsns.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements UserDetails { //USER DTO 이다

    private Integer id;
    private String username;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static UserDto fromEntity(UserEntity entity) {
        return new UserDto(
                entity.getId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRole(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }

    //UserDetails 인터페이스는 시큐리티에서 인증 및 권한 부여를 위해 사용되는 인터페이스중 하나이다.
    //이 인터페이스를 구현함으로서 시큐링티에서 요구하는 사용자 정보를 제공할 수 있다.
    //UserDetails를 통해 사용자 정보드를 가져올 수 있다.
    //UserDetails 구현체
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.toString()));
    }

//    @Override
//    public String getUsername() {
//        return this.username;
//    }
//    //UserDetails 구현체
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return removedAt == null;
    }
    //UserDetails 구현체
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return removedAt == null;
    }
    //UserDetails 구현체
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return removedAt == null;
    }
    //UserDetails 구현체
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return removedAt == null;
    }
}


