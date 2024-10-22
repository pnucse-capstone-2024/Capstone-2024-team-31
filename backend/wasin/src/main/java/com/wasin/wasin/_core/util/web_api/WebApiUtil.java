package com.wasin.wasin._core.util.web_api;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import com.wasin.wasin.domain.dto.CompanyDTO;
import com.wasin.wasin.domain.dto.RouterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebApiUtil {

    private final WebClient webClient;

    @Value("${security.open-api.secret-key}")
    String serviceKey;

    @Value("${security.open-api.token}")
    String apiToken;

    public CompanyDTO.ResponseValue getCompanyList(String name) {
        try {
            return webClient.mutate()
                    .build()
                    .get()
                    .uri("http://apis.data.go.kr/1160100/service/GetCorpBasicInfoService_V2/getCorpOutline_V2", getURI(name))
                    .header("Authorization", "Bearer " + apiToken)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                              clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .map(body -> new ServerException(BaseException.COMPANY_OPEN_API_FAIL)))
                    .bodyToMono(CompanyDTO.ResponseValue.class)
                    .block();
        } catch(Exception e) {
            log.debug(e.getMessage());
            throw new ServerException(BaseException.COMPANY_OPEN_API_FAIL);
        }
    }

    public RouterResponse.prometheusRouter getPrometheusRouter() {
        try {
            return webClient.mutate()
                    .build()
                    .get()
                    .uri("http://prometheus.daily-cotidie.com/api/v1/series?match[]=wifi_network_quality")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .map(body -> new ServerException(BaseException.COMPANY_OPEN_API_FAIL)))
                    .bodyToMono(RouterResponse.prometheusRouter.class)
                    .block();
        } catch(Exception e) {
            log.debug(e.getMessage());
            throw new ServerException(BaseException.PROMETHUS_SERVER_FAIL);
        }
    }

    private Function<UriBuilder, URI> getURI(String name) {
        return uriBuilder -> uriBuilder
                .queryParam("corpNm", URLEncoder.encode(name))
                .queryParam("pageNo", 1)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 100000)
                .queryParam("resultType", "json")
                .build();
    }
}
