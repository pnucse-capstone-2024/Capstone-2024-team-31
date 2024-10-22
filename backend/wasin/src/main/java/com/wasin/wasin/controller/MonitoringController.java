package com.wasin.wasin.controller;

import com.wasin.wasin._core.security.CustomUserDetails;
import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.MonitorResponse;
import com.wasin.wasin.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    // 다중 라우터 모니터링
    @GetMapping()
    public ResponseEntity<?> monitorMultiple(@RequestParam(value = "metricId", required = false) Long metricId,
                                             @RequestParam(value = "time", required = false) Long time,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        MonitorResponse.FindMultiple response = monitoringService.monitorMultiple(metricId, time, userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 단일 모니터 라우터링
    @GetMapping("/{router_id}")
    public ResponseEntity<?> monitorById(@PathVariable(name="router_id") Long routerId,
                                         @RequestParam(value = "metricId", required = false) Long metricId,
                                         @RequestParam(value = "time", required = false) Long time,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        MonitorResponse.FindById response = monitoringService.monitorById(routerId, metricId, time, userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

}
