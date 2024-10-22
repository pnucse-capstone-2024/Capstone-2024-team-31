package com.wasin.wasin._core.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.entity.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTProvider {
    public final Long ACCESS_TOKEN_EXP = 1000L * 60 * 30; // 30분
    public final Long REFRESH_TOKEN_EXP = 1000L * 60 * 60 * 24 * 365; // 1년

    public final String TOKEN_PREFIX = "Bearer ";
    public final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${security.jwt-config.secret.access}")
    public String ACCESS_TOKEN_SECRET;

    @Value("${security.jwt-config.secret.refresh}")
    private String REFRESH_TOKEN_SECRET;

    public String createAccessToken(User user) {
        return TOKEN_PREFIX + createToken(user, ACCESS_TOKEN_EXP, ACCESS_TOKEN_SECRET);
    }

    public String createRefreshToken(User user) {
        return createToken(user, REFRESH_TOKEN_EXP, REFRESH_TOKEN_SECRET);
    }

    public DecodedJWT verifyAccessToken(String jwt) throws SignatureVerificationException, TokenExpiredException {
        jwt = jwt.replace(TOKEN_PREFIX, "");
        return JWT.require(Algorithm.HMAC512(ACCESS_TOKEN_SECRET)).build().verify(jwt);
    }

    public DecodedJWT verifyRefreshToken(String jwt) throws SignatureVerificationException, TokenExpiredException {
        return JWT.require(Algorithm.HMAC512(REFRESH_TOKEN_SECRET)).build().verify(jwt);
    }

    public User getUserByDecodedToken(DecodedJWT decodedJWT) {
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        String email = decodedJWT.getClaim("email").asString();

        return User.builder().id(id).email(email).role(Role.valueOfRole(role)).build();
    }

    private String createToken(User user, Long EXP, String secret) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().getEng())
                .sign(Algorithm.HMAC512(secret));
    }
}
