package com.wasin.wasin.controller;

import com.wasin.wasin._core.security.CustomUserDetails;
import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.RouterRequest;
import com.wasin.wasin.domain.dto.RouterResponse;
import com.wasin.wasin.service.RouterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/router")
public class RouterController {

    private final RouterService routerService;

    @GetMapping("")
    public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        RouterResponse.FindALl response = routerService.findAll(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/{router_id}")
    public ResponseEntity<?> findByRouterId(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(name="router_id") Long routerId) {
        RouterResponse.FindByRouterId response = routerService.findByRouterId(userDetails.getUser(), routerId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody @Valid RouterRequest.CreateDTO requestDTO) {
        routerService.create(userDetails.getUser(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PutMapping("/{router_id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody @Valid RouterRequest.UpdateDTO requestDTO,
                                    @PathVariable(name="router_id") Long router_id) {
        routerService.update(userDetails.getUser(), requestDTO, router_id);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/delete/{router_id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable(name="router_id") Long routerId) {
        routerService.delete(userDetails.getUser(), routerId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/image")
    public ResponseEntity<?> findCompanyImage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        RouterResponse.CompanyImageDTO response = routerService.findCompanyImage(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 라우터 점검 기능
    @GetMapping("/check/{router_id}")
    public ResponseEntity<?> checkRouter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable(name="router_id") Long routerId) {
        RouterResponse.CheckRouter response = routerService.checkRouter(userDetails.getUser(), routerId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 라우터 로그 조회
    @GetMapping("/log/{router_id}")
    public ResponseEntity<?> logRouter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @PathVariable(name="router_id") Long routerId) {
        RouterResponse.LogRouter response = routerService.logRouter(userDetails.getUser(), routerId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 로그 이메일 전송
    @PostMapping("/log/email/{router_id}")
    public ResponseEntity<?> logEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @RequestBody @Valid RouterRequest.LogDTO requestDTO,
                                      @PathVariable(name="router_id") Long routerId) {
        routerService.logEmail(userDetails.getUser(), requestDTO, routerId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
