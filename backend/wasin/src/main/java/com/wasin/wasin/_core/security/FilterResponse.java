package com.wasin.wasin._core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wasin.wasin._core.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterResponse {
    private final ObjectMapper om;

    @Autowired
    public FilterResponse(ObjectMapper om) {
        this.om = om;
    }

    public void writeResponse(HttpServletResponse response, CustomException e) throws IOException {
        response.setStatus(e.status().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(om.writeValueAsString(e.body()));
    }
}
