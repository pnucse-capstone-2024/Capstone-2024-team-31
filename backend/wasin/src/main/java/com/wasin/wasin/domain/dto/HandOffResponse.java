package com.wasin.wasin.domain.dto;

import java.util.List;

public class HandOffResponse {

    public record UserRouter(
            Boolean isAuto,
            List<RouterWithStateDTO> routerList
    ) {
    }

    public record BestRouter(
            Boolean isAuto,
            RouterWithStateDTO router
    ) {
    }

    public record RouterWithStateDTO(
            Long level,
            Long detailLevel,
            Long score,
            String ssid,
            String macAddress,
            String password,
            Boolean isSystemExist
    ) implements Comparable<RouterWithStateDTO> {
        @Override
        public int compareTo(RouterWithStateDTO o) {
            if (this.score.equals(o.score)) {
                return (int) (o.detailLevel - this.detailLevel);
            }
            return (int) (o.score - this.score);
        }
    }
}
