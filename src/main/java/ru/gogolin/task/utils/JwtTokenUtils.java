package ru.gogolin.task.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.gogolin.task.dtos.AuthenticationUserDto;
import ru.gogolin.task.entities.Role;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expired}")
    private Duration jwtLifetime;

    public String generateToken(AuthenticationUserDto authenticationUserDto) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = authenticationUserDto.roles()
                .stream()
                .map(Role::getName)
                .toList();
        claims.put("roles", roleList);
        Date ussuedDate = new Date();
        Date expiredDate = new Date(ussuedDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authenticationUserDto.email())
                .setIssuedAt(ussuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}
