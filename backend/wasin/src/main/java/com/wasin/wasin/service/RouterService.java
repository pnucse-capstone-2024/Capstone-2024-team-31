package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.RouterRequest;
import com.wasin.wasin.domain.dto.RouterResponse;
import com.wasin.wasin.domain.entity.User;

public interface RouterService {

    RouterResponse.FindALl findAll(User user);

    RouterResponse.FindByRouterId findByRouterId(User user, Long routerId);

    void create(User user, RouterRequest.CreateDTO requestDTO);

    void update(User user, RouterRequest.UpdateDTO requestDTO, Long routerId);

    void delete(User user, Long routerId);

    RouterResponse.CompanyImageDTO findCompanyImage(User user);

    RouterResponse.CheckRouter checkRouter(User user, Long routerId);

    RouterResponse.LogRouter logRouter(User user, Long routerId);

    void logEmail(User user, RouterRequest.LogDTO requestDTO, Long routerId);
}
