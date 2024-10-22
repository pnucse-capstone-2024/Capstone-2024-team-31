package com.wasin.wasin.controller;

import com.wasin.wasin._core.security.CustomUserDetails;
import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.HandOffRequest;
import com.wasin.wasin.domain.dto.HandOffResponse;
import com.wasin.wasin.service.HandOffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hand-off")
public class HandOffController {

    private final HandOffService handOffService;

    @PostMapping("")
    public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestBody @Valid HandOffRequest.UserRouterDTO requestDTO) {
        HandOffResponse.UserRouter response = handOffService.findAll(userDetails.getUser(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("/best")
    public ResponseEntity<?> findBestRouter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody @Valid HandOffRequest.UserRouterDTO requestDTO) {
        HandOffResponse.BestRouter response = handOffService.findBestRouter(userDetails.getUser(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("/mode/auto")
    public ResponseEntity<?> changeModeAuto(@AuthenticationPrincipal CustomUserDetails userDetails) {
        handOffService.changeModeAuto(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 모드 수동으로 변경
    @PostMapping("/mode/manual")
    public ResponseEntity<?> changeModeManual(@AuthenticationPrincipal CustomUserDetails userDetails) {
        handOffService.changeModeManual(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
