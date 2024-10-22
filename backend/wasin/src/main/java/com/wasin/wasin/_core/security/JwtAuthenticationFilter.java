package com.wasin.wasin._core.security;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wasin.wasin.domain.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JWTProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(jwtProvider.AUTHORIZATION_HEADER);
        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }
        if (!accessToken.startsWith(jwtProvider.TOKEN_PREFIX)) {
            throw new JWTDecodeException("토큰 형식이 잘못되었습니다.");
        }

        try{
            DecodedJWT decodedJWT = jwtProvider.verifyAccessToken(accessToken);
            User user = jwtProvider.getUserByDecodedToken(decodedJWT);
            CustomUserDetails userDetails = new CustomUserDetails(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 인증 객체 만들어짐");
        } catch (SignatureVerificationException e) {
            log.error("토큰 검증 실패");
            throw new JWTVerificationException("토큰 검증이 실패했습니다.");
        } catch (TokenExpiredException e) {
            log.error("토큰 만료 됨");
            //throw new UnauthorizedException(BaseException.ACCESS_TOKEN_EXPIRED);
        } catch (JWTDecodeException e) {
            log.error("잘못된 토큰");
            throw new JWTDecodeException("토큰 형식이 잘못되었습니다.");
        }
        chain.doFilter(request, response);
    }

}
