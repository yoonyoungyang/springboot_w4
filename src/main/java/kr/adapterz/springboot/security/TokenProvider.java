package kr.adapterz.springboot.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {
    private final Key key;
    private final long expirationHours;
    private final String issuer;

    public TokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
    @Value("${jwt.expirationHours}") long expirationHours,
    @Value("${jwt.issuer}") String issuer
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expirationHours = expirationHours;
        this.issuer = issuer;
    }

    public String createToken(String userSpecification) {
        return Jwts.builder()
                .signWith(key)   // HS512 알고리즘을 사용하여 key를 이용해 서명
                .setSubject(userSpecification)  // JWT 토큰이 나타내는 사용자 식별 정보
                .setIssuer(issuer)  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }
    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
