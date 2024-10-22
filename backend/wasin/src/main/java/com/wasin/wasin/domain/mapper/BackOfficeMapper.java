package com.wasin.wasin.domain.mapper;

import com.wasin.wasin.domain.dto.BackOfficeResponse;
import com.wasin.wasin.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BackOfficeMapper {

    public BackOfficeResponse.WaitingList findWaitingList(List<User> adminList) {
        return new BackOfficeResponse.WaitingList(
                adminList.stream()
                        .map(a -> new BackOfficeResponse.WaitingList.WaitingItem(a.getId(), a.getUsername()))
                        .collect(Collectors.toList())
        );
    }

}
