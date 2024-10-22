package com.wasin.wasin.controller;

import com.wasin.wasin._core.util.ApiUtils;
import com.wasin.wasin.domain.dto.AlertRequest;
import com.wasin.wasin.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alert")
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/notification")
    public ResponseEntity<?> receiveAlert(@RequestBody AlertRequest.ProfileChangeDTO request) {
        alertService.receiveAlert(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

}
