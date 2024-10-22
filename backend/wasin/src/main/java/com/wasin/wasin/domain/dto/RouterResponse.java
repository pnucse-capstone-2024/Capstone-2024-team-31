package com.wasin.wasin.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class RouterResponse {
    public record FindALl(CompanyImageDTO image, List<EachRouter> routerList) { }

    public record FindByRouterId(CompanyImageDTO image, RouterInformationDTO information) { }

    public record prometheusRouter(String status, List<WifiNetworkQuality> data) { }

    public record WifiNetworkQuality(String __name__, String bssid, String channel, String country, String device,
                 String frequency, String ifname, String instance, String job, String mode, String ssid) { }

    public record EachRouter (Long routerId, String name, Long score, Double positionX, Double positionY) { }

    public record CompanyImageDTO(String companyImage, Integer imageHeight, Integer imageWidth){ }

    public record RouterInformationDTO(String name, String ssid, String macAddress, String instance, String serialNumber,
                                       String password, Integer port, Long score, Double positionX, Double positionY){ }

    public record RouterResult(Results results) {}

    public record Results(Instant A) {}

    public record Instant(int status, List<Frame> frames) {}

    public record Frame(Data data) {}

    public record Data(List<List<BigDecimal>> values) {}

    public record Queries(List<Query> queries, String from, String to) {}

    public record Query(String refId, String expr, Boolean instant, Datasource datasource) {}

    public record MonitorResult(MonitorResults results) {}

    public record MonitorResults(MonitorInstant A) {}

    public record MonitorInstant(int status, List<MonitorFrame> frames) {}

    public record MonitorFrame(Schema schema, Data data) {}

    public record Schema(List<Field> fields) {}

    public record Field(String name, String type, Label labels) {}

    public record Label(String device, String instance, String job) {}

    public record MonitoringQueries(List<MonitoringQuery> queries, String from, String to) {}

    public record MonitoringQuery(Datasource datasource, String expr, String format, String refId, Long utcOffsetSec, Long maxDataPoints) {}

    public record Datasource(String type, String uid) {}

    public record CheckRouter(String result) {}

    public record LogRouter(String log) {}

}
