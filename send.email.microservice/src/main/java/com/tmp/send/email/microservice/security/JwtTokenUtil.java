package com.tmp.send.email.microservice.security;

import com.tmp.send.email.microservice.exceptions.RoleDoesNotExistException;
import com.tmp.send.email.microservice.models.enums.RoleValue;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

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

    public List<RoleValue> getRolesFromToken(String token) {
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
                            return RoleValue.EMPLOYEE;
                        case "ADMIN":
                            return RoleValue.ADMIN;
                        case "MANAGER":
                            return RoleValue.MANAGER;
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
