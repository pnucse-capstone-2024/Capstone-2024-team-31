package com.wasin.wasin.domain.dto;

import java.util.List;

public class AlertRequest {
    public record ProfileChangeDTO(
            List<AlertDTO> alerts
    ) {
    }

    public record AlertDTO(
        LabelDTO labels
    ) {
    }

    public record LabelDTO(
        String alertname,
        String grafana_folder,
        String instance
    ) {
    }
}
