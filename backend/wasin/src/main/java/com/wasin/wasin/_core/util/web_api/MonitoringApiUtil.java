package com.wasin.wasin._core.util.web_api;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import com.wasin.wasin.domain._const.Metric;
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
public class MonitoringApiUtil {

    private final WebClient webClient;

    @Value("${jang.header}")
    String grafanaHeader;

    @Value("${jang.datasource-uid}")
    String grafanaDatasourceUID;

    public RouterResponse.MonitorResult getMetric(Metric metric, Router router, Long time) {
        String expr = getExpression(metric, router);
        RouterResponse.MonitoringQueries monitoringQuery = getMonitoringQuery(expr, time);
        return getResult(monitoringQuery);
    }

    public RouterResponse.MonitorResult getMetricInRangeTime(Metric metric, Router router, Long time) {
        String expr = getExpression(metric, router);
        RouterResponse.MonitoringQueries monitoringQuery = getMonitoringQuery(expr, time);
        return getResult(monitoringQuery);
    }

    private String getExpression(Metric metric, Router router) {
        return switch (metric) {
            case WIFI_SCORE -> getWifiScoreExpr(router);
            case WIFI_CLIENT -> getWifiClientsExpr(router);
            case CPU_SYSTEM -> getCPUSystemExpr(router);
            case WIFI_NETWORK_NOISE -> getNetworkNoiseExpr(router);
            case WIFI_BITRATE -> getWifiBitRateExpr(router);
            case NETWORK_TRAFFIC_BY_BYTES -> getNetworkTrafficByBytesExpr(router);
            case NETWORK_TRAFFIC_ERRORS -> getNetworkTrafficErrorsExpr(router);
        };
    }

    private String getWifiScoreExpr(Router router) {
        return "scalar(1 - (avg(node_load1{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) " +
                "/ count(count(node_cpu_seconds_total{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) by (cpu)))) " +
                "* (1 - 0.3 * (wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"} " +
                "- min_over_time(wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"}[3h])) " +
                "/ (1 + max_over_time(wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"}[3h]) " +
                "- min_over_time(wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\", ifname=\"wlan0\"}[3h]))) * 100";
    }

    private String getWifiClientsExpr(Router router) {
        return "sum(wifi_stations{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"})";
    }

    private String getCPUSystemExpr(Router router) {
        return "avg(node_load1{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) / " +
                " count(count(node_cpu_seconds_total{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}) by (cpu)) * 100";
    }

    private String getNetworkNoiseExpr(Router router) {
        return "wifi_network_noise_dbm{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}";
    }

    private String getWifiBitRateExpr(Router router) {
        return "wifi_network_bitrate{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}/1024";
    }

    private String getNetworkTrafficByBytesExpr(Router router) {
        return "irate(node_network_receive_bytes_total{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() +  "\"}[5m])/1024";
    }

    private String getNetworkTrafficErrorsExpr(Router router) {
        return "irate(node_network_receive_errs_total{instance=~\"" + router.getInstance() + "\",job=~\"" + router.getJob() + "\"}[5m])";
    }

    private RouterResponse.MonitorResult getResult(RouterResponse.MonitoringQueries requestDTO) {
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
                                            .map(body -> {
                                                log.debug(body);
                                                return new ServerException(BaseException.GRAFANA_REQUEST_FAIL);
                                            }))
                            .bodyToMono(RouterResponse.MonitorResult.class)
                            .doOnSuccess(result -> log.debug(result.toString()))
                            .block());
        } catch(Exception e) {
            log.debug(e.getMessage());
            throw new ServerException(BaseException.GRAFANA_SERVER_FAIL);
        }
    }

    private RouterResponse.MonitoringQueries getMonitoringQuery(String expr, Long time) {
        RouterResponse.Datasource datasource = new RouterResponse.Datasource("prometheus", grafanaDatasourceUID);
        RouterResponse.MonitoringQuery query = getQuery(expr, datasource);

        return new RouterResponse.MonitoringQueries(
                List.of(query),
                "now-" + time + "m",
                "now"
        );
    }

    private static RouterResponse.MonitoringQuery getQuery(String expr, RouterResponse.Datasource datasource) {
        return new RouterResponse.MonitoringQuery(
                datasource,
                expr,
                "time_series",
                "A",
                0L,
                1000L
        );
    }

}
