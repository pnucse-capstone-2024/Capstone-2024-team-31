package com.wasin.wasin.domain.mapper;

import com.wasin.wasin._core.util.web_api.GrafanaApiUtil;
import com.wasin.wasin.domain.dto.RouterRequest;
import com.wasin.wasin.domain.dto.RouterResponse;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.CompanyImage;
import com.wasin.wasin.domain.entity.Router;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RouterMapper {

    private final GrafanaApiUtil grafanaApiUtil;

    public Router dtoToRouter(RouterRequest.CreateDTO requestDTO,
                              RouterResponse.WifiNetworkQuality prometheusRouter,
                              Company company) {
        return Router.builder()
                .macAddress(prometheusRouter.bssid().toLowerCase())
                .name(requestDTO.name())
                .ssid(prometheusRouter.ssid())
                .instance(prometheusRouter.instance())
                .serialNumber(requestDTO.serialNumber())
                .password(requestDTO.password())
                .port(requestDTO.port())
                .job(prometheusRouter.job())
                .positionX(requestDTO.positionX())
                .positionY(requestDTO.positionY())
                .company(company)
                .build();
    }

    public RouterResponse.FindByRouterId routerToRouterByIdDTO(CompanyImage companyImage, Router router) {
        return new RouterResponse.FindByRouterId(
                new RouterResponse.CompanyImageDTO(
                        companyImage.getUrl(),
                        companyImage.getHeight(),
                        companyImage.getWidth()
                ),
                new RouterResponse.RouterInformationDTO(
                        router.getName(),
                        router.getSsid(),
                        router.getMacAddress(),
                        router.getInstance(),
                        router.getSerialNumber(),
                        router.getPassword(),
                        router.getPort(),
                        grafanaApiUtil.getRouterState(router),
                        router.getPositionX(),
                        router.getPositionY()
                )
        );
    }

    public RouterResponse.FindALl routerFindAllDTO(CompanyImage companyImage, List<Router> router) {
        return new RouterResponse.FindALl(
                new RouterResponse.CompanyImageDTO(
                        companyImage.getUrl(),
                        companyImage.getHeight(),
                        companyImage.getWidth()
                ),
                router.stream().map(r -> new RouterResponse.EachRouter(
                        r.getId(),
                        r.getName(),
                        grafanaApiUtil.getRouterState(r),
                        r.getPositionX(),
                        r.getPositionY()
                )).toList()
        );
    }

    public RouterResponse.CompanyImageDTO imageEntityToDTO(CompanyImage image) {
        return new RouterResponse.CompanyImageDTO(
                image.getUrl(),
                image.getHeight(),
                image.getWidth()
        );
    }
}
