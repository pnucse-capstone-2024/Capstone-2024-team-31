package com.wasin.wasin._core.security;


import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wasin.wasin._core.exception.error.ForbiddenException;
import com.wasin.wasin._core.exception.error.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final FilterResponse filterResponse;

    @Autowired
    public JwtExceptionFilter(FilterResponse filterResponse) {
        this.filterResponse = filterResponse;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        }
        catch (ForbiddenException e) {
            filterResponse.writeResponse(response, e);
        }
        catch (JWTCreationException | JWTVerificationException e) {
            filterResponse.writeResponse(response, new UnauthorizedException(e.getMessage()));
        }
    }
}
