package com.wasin.wasin.controller;

import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.service.MailService;
import com.wasin.wasin.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class AuthController {

    private final TokenService tokenService;
    private final MailService mailService;

    // 토큰 리프레시
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody @Valid UserRequest.ReissueDTO requestDTO) {
        UserResponse.Token response = tokenService.reissue(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 이메일 인증 코드 전송
    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody @Valid UserRequest.EmailDTO requestDTO) {
        mailService.sendMail(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 이메일 인증 확인
    @PostMapping("/email/check")
    public ResponseEntity<?> checkEmailCode(@RequestBody @Valid UserRequest.EmailCheckDTO requestDTO) {
        mailService.checkMailCode(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
