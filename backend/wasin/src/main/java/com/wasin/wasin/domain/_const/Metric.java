package com.wasin.wasin.domain._const;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum Metric {
    WIFI_SCORE("와이파이 점수", "높을 수록 쾌적한 네트워크 성능을 기대할 수 있어요.", 1L),
    WIFI_CLIENT("연결된 디바이스 수", "해당 공유기에 와이파이를 연결한 디바이스의 수를 나타내요.", 2L),
    CPU_SYSTEM("시스템에 가해지는 부하의 양", "네트워크 처리량이 많아질 경우 부하가 커져요.", 3L),
    WIFI_NETWORK_NOISE("평균 노이즈량", "현재 와이파이에 연결한 디바이스들의 편균 노이즈량을 나타내요.", 4L),
    WIFI_BITRATE("Bit 양", "와이파이로 주고받는 Bit의 양을 나타내요.", 5L),
    NETWORK_TRAFFIC_BY_BYTES("데이터의 바이트 크기", "각 LAN/WAN으로 송수신되는 데이터의 바이트 크기를 나타내요.", 6L),
    NETWORK_TRAFFIC_ERRORS("에러 발생 패킷 수", "네트워크 환경에서 에러가 발생한 패킷 수를 나타내요.", 7L),
    ;

    private final String kor;

    private final String description;

    private final Long index;

    public static Metric findByMetricId(Long metricId) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.index, metricId))
                .findFirst()
                .orElse(Metric.WIFI_SCORE);
    }
}
