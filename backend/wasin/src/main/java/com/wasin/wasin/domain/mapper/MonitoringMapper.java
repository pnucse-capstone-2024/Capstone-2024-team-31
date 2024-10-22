package com.wasin.wasin.domain.mapper;

import com.wasin.wasin.domain._const.Metric;
import com.wasin.wasin.domain.dto.MonitorResponse;
import com.wasin.wasin.domain.dto.RouterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RequiredArgsConstructor
@Component
public class MonitoringMapper {

    public MonitorResponse.FindById resultToDTO(RouterResponse.MonitorResult results, Long metricId, Long time) {
        return new MonitorResponse.FindById(
                metricId,
                time,
                getMetricList(),
                getGraphDataList(results)
        );
    }


    public MonitorResponse.FindMultiple resultToMultipleDTO(List<RouterResponse.MonitorResult> results, Long metricId, Long time) {
        return new MonitorResponse.FindMultiple(
                metricId,
                time,
                getMetricList(),
                getAverageGraphDataList(results)
        );
    }

    private List<MonitorResponse.MonitorGraph> getAverageGraphDataList(List<RouterResponse.MonitorResult> results) {
        Map<String, Map<BigDecimal, List<BigDecimal>>> deviceTimeValueMap = getDeviceTimeValueMap(results);
        return getMonitorGraphList(deviceTimeValueMap);
    }

    private List<MonitorResponse.MonitorGraph> getMonitorGraphList(Map<String, Map<BigDecimal, List<BigDecimal>>> deviceTimeValueMap) {
        List<MonitorResponse.MonitorGraph> monitorGraphList = new ArrayList<>();

        deviceTimeValueMap.forEach((device, timeValueMap) -> {
            List<BigDecimal> timeList = new ArrayList<>();
            List<BigDecimal> averageValueList = new ArrayList<>();

            timeValueMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(entry -> {
                BigDecimal time = entry.getKey();
                List<BigDecimal> values = entry.getValue();

                BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal averageValue = values.isEmpty() ? BigDecimal.ZERO
                        : sum.divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP);
                timeList.add(time);
                averageValueList.add(averageValue);
            });

            monitorGraphList.add(new MonitorResponse.MonitorGraph(
                    device,
                    timeList,
                    averageValueList
            ));
        });
        return monitorGraphList;
    }

    private Map<String, Map<BigDecimal, List<BigDecimal>>> getDeviceTimeValueMap(List<RouterResponse.MonitorResult> results) {
        Map<String, Map<BigDecimal, List<BigDecimal>>> deviceTimeValueMap = new HashMap<>();

        results.forEach(result -> result.results().A().frames().forEach(frame -> {
            RouterResponse.Label label = frame.schema().fields().get(1).labels();
            String device = label.device() == null ? "" : label.device();

            List<BigDecimal> timeList = frame.data().values().get(0);
            List<BigDecimal> valueList = frame.data().values().get(1);

            deviceTimeValueMap.putIfAbsent(device, new HashMap<>());
            for (int i = 0; i < timeList.size(); i++) {
                BigDecimal time = timeList.get(i);
                BigDecimal value = valueList.get(i);

                deviceTimeValueMap.get(device).putIfAbsent(time, new ArrayList<>());
                deviceTimeValueMap.get(device).get(time).add(value);
            }
        }));
        return deviceTimeValueMap;
    }

    private List<MonitorResponse.MonitorGraph> getGraphDataList(RouterResponse.MonitorResult results) {
        return results.results().A().frames().stream().map(it -> {
                    RouterResponse.Label label = it.schema().fields().get(1).labels();
                    return new MonitorResponse.MonitorGraph(
                            label.device() == null ? "" : label.device(),
                            it.data().values().get(0),
                            it.data().values().get(1)
                    );
                }
        ).toList();
    }

    private List<MonitorResponse.MonitorMetric> getMetricList() {
        return Arrays.stream(Metric.values()).map(it ->
                new MonitorResponse.MonitorMetric(
                        it.getKor(),
                        it.getIndex()
                )
        ).toList();
    }

}
