package com.wasin.wasin.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class MonitorResponse {
    public record FindById(
            Long activeMetricId,
            Long settingTime,
            List<MonitorMetric> metricList,
            List<MonitorGraph> graphList
    ) {
    }

    public record FindMultiple(
            Long activeMetricId,
            Long settingTime,
            List<MonitorMetric> metricList,
            List<MonitorGraph> graphList
    ){
    }

    public record MonitorMetric(
        String metric,
        Long metricId
    ){
    }

    public record MonitorGraph(
        String labels,
        List<BigDecimal> timeList,
        List<BigDecimal> valueList
    ) {
    }

}
