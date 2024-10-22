package com.wasin.wasin.domain.mapper;

import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.entity.enums.Role;
import com.wasin.wasin.domain.entity.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User signupToUser(UserRequest.SignUpDTO requestDTO) {
        Role role = Role.valueOfRole(requestDTO.role());
        Status status = Status.getDefaultStatusByRole(role);
        String password = passwordEncoder.encode(requestDTO.password());

        return User.builder()
                .email(requestDTO.email())
                .password(password)
                .username(requestDTO.username())
                .role(role)
                .status(status)
                .isModeAuto(true)
                .build();
    }
}
