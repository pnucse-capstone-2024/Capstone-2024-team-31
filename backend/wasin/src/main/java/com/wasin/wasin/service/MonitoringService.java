package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.MonitorResponse;
import com.wasin.wasin.domain.entity.User;

public interface MonitoringService {

    MonitorResponse.FindById monitorById(Long routerId, Long metricId, Long time, User user);

    MonitorResponse.FindMultiple monitorMultiple(Long metricId, Long time, User user);
}
