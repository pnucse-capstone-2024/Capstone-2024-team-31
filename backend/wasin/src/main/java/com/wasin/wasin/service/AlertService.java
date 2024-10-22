package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.AlertRequest;

public interface AlertService {
    void receiveAlert(AlertRequest.ProfileChangeDTO request);
}
