package com.wasin.wasin.service.impl;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.security.JWTProvider;
import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.domain.entity.Token;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.mapper.TokenMapper;
import com.wasin.wasin.domain.validation.TokenValidation;
import com.wasin.wasin.repository.TokenRepository;
import com.wasin.wasin.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final TokenValidation tokenValidation;
    private final JWTProvider jwtProvider;
    private final TokenMapper tokenMapper;

    public UserResponse.Token save(User user) {
        return createNewToken(user);
    }

    public void delete(String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken).orElseThrow(
                () -> new NotFoundException(BaseException.ACCESS_TOKEN_NOT_FOUND)
        );
        tokenRepository.delete(token);
    }

    public UserResponse.Token reissue(UserRequest.ReissueDTO requestDTO) {
        String refreshToken = requestDTO.refreshToken();

        // 리프레시 토큰이 존재하는지 검증
        tokenValidation.checkRefreshTokenExist(refreshToken);

        // 리프레시 토큰으로 유저 찾기
        User user = getUserByRefreshToken(refreshToken);

        // 리프레시 토큰 삭제하기
        tokenRepository.deleteById(refreshToken);
        
        // 토큰(accessToken, refreshToken) 새로 생성하기
        return createNewToken(user);
    }

    private User getUserByRefreshToken(String refreshToken) {
        try{
            DecodedJWT decodedJWT = jwtProvider.verifyRefreshToken(refreshToken);
            return jwtProvider.getUserByDecodedToken(decodedJWT);
        } catch (SignatureVerificationException | JWTDecodeException e) {
            throw new BadRequestException(BaseException.REFRESH_TOKEN_INVALID);
        } catch (TokenExpiredException tee) {
            throw new BadRequestException(BaseException.REFRESH_TOKEN_EXPIRED);
        }
    }

    private UserResponse.Token createNewToken(User user) {
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        Token token = tokenMapper.stringToRefreshToken(accessToken, refreshToken, user);
        tokenRepository.save(token);

        return new UserResponse.Token(accessToken, refreshToken);
    }


}
