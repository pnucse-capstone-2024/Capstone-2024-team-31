package com.wasin.wasin.domain.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class BackOfficeResponse {

    public record WaitingList(
            List<WaitingItem> waitingList
    ) {
        public record WaitingItem(
            Long userId,
            String name
        ) {
        }
    }

}
