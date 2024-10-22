package com.wasin.wasin._core.security;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(
            userJPARepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException(BaseException.USER_EMAIL_NOT_FOUND.getMessage())
            )
        );
    }
}
