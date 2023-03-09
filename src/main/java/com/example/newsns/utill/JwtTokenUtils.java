package com.example.newsns.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    //토큰값을 통해 고객식벽자 username 을 얻는 로직
    public static String getUserName(String token,String key){
       return extractAllClaims(token, key).get("userName", String.class);
    }
   //토큰값을 통해 만료시간이 유효한지 확인하는 로직
    public static boolean isExpired(String token, String key){
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
        //expiration 의 시간이 현재 시간보다 이전인지 여부를 확인하여 토큰이 만료되었는지 여부를 boolean 값으로 반환
    }

    //암호화된 토큰을 분석하여 원래데이터로 반환하는 메서드
    private static Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder()// parserBuilder 를 통해 JWT 파서(JWT Parser)는 구문분석하고 검증하기 위한 도구이다.
                .setSigningKey(getKey(key))//get(key)값을 불러와 파서에게 셋팅한다..
                .build()
                .parseClaimsJws(token)//전달받은 jwt토큰을 파싱하고 검증한다.
                .getBody(); //검증받은 토큰을 통해 추출된 데이터를 암호화 되기전에 원래데이터로 반환한다.
    }

    //토큰을 만들때 그 안에다 USERNAME 을 만든다. key 는 USERNAME 을 넣은 값을 암호화할때 쓰는 키 , 유효타임 expiredTimeMS
    public static String generateToken(String userName, String key, long expiredTimeMS){
        //Jwt 라이브러리를 선언해야 Claims 를 쓸수 있다.
        Claims claims = Jwts.claims();
        claims.put("userName", userName);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))//토큰이 발행된 시각
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMS))
                .signWith(getKey(key), SignatureAlgorithm.HS256) //key 값을 얻어서 해시해서 알고리즘을 사용하도록한다.
                .compact();
    }

    // 보안을 위해 사용되는 암호화 키를 나타내는 클래스.

    private static Key getKey (String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        //key를 바이트 배열로 변환한 다음,
        return Keys.hmacShaKeyFor(keyBytes);
        //해당 키를 사용하여 HMAC SHA 알고리즘에 사용되는 javax.crypto.SecretKey를 생성하여 반환
        //Keys.hmacShaKeyFor(keyBytes) 메서드는 keyBytes를
        // 사용하여 HMAC SHA 알고리즘에 사용될 수 있는 javax.crypto.SecretKey를 생성
    }
}
