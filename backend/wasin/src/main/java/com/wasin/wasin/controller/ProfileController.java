package com.wasin.wasin.controller;

import com.wasin.wasin._core.security.CustomUserDetails;
import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.ProfileResponse;
import com.wasin.wasin.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    // 프로파일 조회
    @GetMapping("")
    public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ProfileResponse.FindAll response = profileService.findAll(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 모드 자동으로 변경
    @PostMapping("/mode/auto")
    public ResponseEntity<?> changeModeAuto(@AuthenticationPrincipal CustomUserDetails userDetails) {
        profileService.changeModeAuto(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 모드 수동으로 변경
    @PostMapping("/mode/manual")
    public ResponseEntity<?> changeModeManual(@AuthenticationPrincipal CustomUserDetails userDetails) {
        profileService.changeModeManual(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }


    // 프로파일 선택
    @PostMapping("/{profile_id}")
    public ResponseEntity<?> changeProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable(name="profile_id") Long profile_id) {
        profileService.changeProfile(userDetails.getUser(), profile_id);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

}
