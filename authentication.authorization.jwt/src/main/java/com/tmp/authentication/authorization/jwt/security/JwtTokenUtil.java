package com.tmp.authentication.authorization.jwt.security;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.models.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
    private String jwtSecret;

    @Value("${jwt.address}:${jwt.port}")
    private String jwtIssuer;

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        List<Roles> roles = user.getRoles().stream()
                .map(Role::getRoles)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return createAccessToken(claims, user.getEmail());
    }

    private String createAccessToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(jwtIssuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return createRefreshToken(user.getEmail());
    }

    private String createRefreshToken(String subject) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuer(jwtIssuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private Claims getAllTokenClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllTokenClaims(token);

        return claims.getSubject();
    }

    public List<Roles> getRolesFromToken(String token) {
        Claims claims = getAllTokenClaims(token);

        String unformattedRoles = claims.get("roles").toString();

        unformattedRoles = unformattedRoles.replace("[", "");
        unformattedRoles = unformattedRoles.replace("]", "");

        List<String> roles = Arrays.stream(unformattedRoles.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        return roles.stream()
                .map(role -> {
                    switch (role.toUpperCase(Locale.ROOT)) {
                        case "EMPLOYEE":
                            return Roles.EMPLOYEE;
                        case "ADMIN":
                            return Roles.ADMIN;
                        case "MANAGER":
                            return Roles.MANAGER;
                        default:
                            throw new RoleDoesNotExistException(role);
                    }
                })
                .collect(Collectors.toList());
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("Expired JWT token - {}", expiredJwtException.getMessage());
        } catch (MalformedJwtException malformedJwtException) {
            log.error("Invalid JWT token - {}", malformedJwtException.getMessage());
        } catch (PrematureJwtException prematureJwtException) {
            log.error("Unacceptable JWT token - {}", prematureJwtException.getMessage());
        } catch (SignatureException signatureException) {
            log.error("Invalid JWT signature - {}", signatureException.getMessage());
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.error("Unsupported JWT token - {}", unsupportedJwtException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            log.error("JWT claims string is empty - {}", illegalArgumentException.getMessage());
        }
        return false;
    }
}