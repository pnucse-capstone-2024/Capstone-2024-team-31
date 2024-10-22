package com.wasin.wasin.domain.dto;

public class MetricResponse {
    public record GetMetricDashboard(
        Meta meta,
        Dashboard dashboard
    ){
        public record Meta (
            String slug,
            String url,
            Boolean canEdit
        ){}
        public record Dashboard (
            String title,
            String uid,
            Long version
        ){}
    }
}
