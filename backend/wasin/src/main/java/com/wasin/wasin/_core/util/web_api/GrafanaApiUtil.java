package com.wasin.wasin._core.util.web_api;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import com.wasin.wasin.domain.dto.RouterResponse;
import com.wasin.wasin.domain.entity.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrafanaApiUtil {

    private final WebClient webClient;

    @Value("${jang.header}")
    String grafanaHeader;

    @Value("${jang.datasource-uid}")
    String grafanaDatasourceUID;

    public Long getWifiState(Router router) {
        String expr = "scalar(1 - (avg(node_load1{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) / " +
                "count(count(node_cpu_seconds_total{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) by (cpu)))) * " +
                "(1 - 0.3 * (wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"} - " +
                "min_over_time(wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"}[3h])) / " +
                "(1 + max_over_time(wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"}[3h]) - " +
                "min_over_time(wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"}[3h]))) * 100";
        RouterResponse.Queries requestDTO = getRequestDTO(expr);
        return getResult(requestDTO);
    }

    public Long getRouterState(Router router) {
        String expr = "100 * (1 - avg(node_load1{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) / " +
                "count(count(node_cpu_seconds_total{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) by (cpu)))";
        RouterResponse.Queries requestDTO = getRequestDTO(expr);
        return getResult(requestDTO);
    }

    public Long getActiveUser(Router router) {
        String expr = "sum(wifi_stations{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"})";
        RouterResponse.Queries requestDTO = getRequestDTO(expr);
        return getResult(requestDTO);
    }

    private Long getResult(RouterResponse.Queries requestDTO) {
        try {
            return Objects.requireNonNull(webClient.mutate()
                            .build()
                            .post()
                            .uri("http://grafana.daily-cotidie.com/api/ds/query")
                            .bodyValue(requestDTO)
                            .header("Authorization", "Bearer " + grafanaHeader)
                            .retrieve()
                            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                    clientResponse -> clientResponse
                                            .bodyToMono(String.class)
                                            .map(body -> new ServerException(BaseException.GRAFANA_REQUEST_FAIL)))
                            .bodyToMono(RouterResponse.RouterResult.class)
                            .block())
                    .results().A().frames().get(0).data().values().get(1).get(0).longValue();
        } catch(Exception e) {
            log.debug(e.getMessage());
            throw new ServerException(BaseException.GRAFANA_SERVER_FAIL);
        }
    }

    private RouterResponse.Queries getRequestDTO(String expr) {
        String type = "prometheus";
        String refId = "A";
        RouterResponse.Queries requestDTO = new RouterResponse.Queries(
                List.of(new RouterResponse.Query(refId, expr, true, new RouterResponse.Datasource(type, grafanaDatasourceUID))),
                "now-5m",
                "now"
        );
        return requestDTO;
    }


}
