package com.tmp.authentication.authorization.jwt.security;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.models.ERole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtTokenUtil {

    public static final long JWT_ACCESS_TOKEN_VALIDITY = 2 * 60 * 1000;                     // 2 minutes
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;          // 7 days

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.address}:${jwt.port}")
    private String issuer;

    private Claims getAllTokenClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        List<ERole> eRoles = user.getRoles().stream()
                .map(Role::getERole)
                .collect(Collectors.toList());

        claims.put("roles", eRoles);

        return createAccessToken(claims, user.getEmail());
    }

    private String createAccessToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return createRefreshToken(user.getEmail());
    }

    private String createRefreshToken(String subject) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuer(issuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllTokenClaims(token);

        return claims.getSubject();
    }

    public ERole getRolesFromToken(String token) {
        Claims claims = getAllTokenClaims(token);

        String role = claims.get("roles").toString();

        switch (role.toUpperCase(Locale.ROOT)) {
            case "EMPLOYEE":
                return ERole.EMPLOYEE;
            case "ADMIN":
                return ERole.ADMIN;
            case "MANAGER":
                return ERole.MANAGER;
            default:
                throw new RoleDoesNotExistException(role);
        }
    }

    public Boolean isTokenExpired(String token) {
        Claims claims = getAllTokenClaims(token);

        return claims.getExpiration().before(new Date());
    }
}