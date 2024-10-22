package com.wasin.wasin.controller;

import com.wasin.wasin._core.security.CustomUserDetails;
import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.service.MailService;
import com.wasin.wasin.service.TokenService;
import com.wasin.wasin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final TokenService tokenService;
    private final UserService userService;
    private final MailService mailService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequest.SignUpDTO requestDTO) {
        mailService.checkVerified(requestDTO.email());
        userService.signup(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO) {
        User user = userService.login(requestDTO);
        UserResponse.Token response = tokenService.save(user);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
        tokenService.delete(accessToken);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 회원탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdraw(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 패스워드 설정
    @PostMapping("/lock")
    public ResponseEntity<?> setLockPassword(@RequestBody @Valid UserRequest.LockDTO requestDTO,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.setLockPassword(requestDTO, userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 패스워드 확인
    @PostMapping("/lock/confirm")
    public ResponseEntity<?> checkLockPassword(@RequestBody @Valid UserRequest.LockConfirmDTO requestDTO,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.checkLockPassword(requestDTO, userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

}
