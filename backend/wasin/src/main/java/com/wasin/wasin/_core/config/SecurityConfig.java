package com.wasin.wasin._core.config;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ForbiddenException;
import com.wasin.wasin._core.exception.error.UnauthorizedException;
import com.wasin.wasin._core.security.FilterResponse;
import com.wasin.wasin._core.security.JWTProvider;
import com.wasin.wasin._core.security.JwtAuthenticationFilter;
import com.wasin.wasin._core.security.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtExceptionFilter jwtExceptionFilter;
    private final FilterResponse filterResponse;
    private final JWTProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider));
            builder.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
            super.configure(builder);
        }

        public HttpSecurity build(){
            return getBuilder();
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf(CsrfConfigurer::disable);

        // 2. iframe 거부
        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 3. cors 재설정
        http.cors((cors) -> cors.configurationSource(configurationSource()));

        // 4. jSessionId 사용 거부
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 5. form 로긴 해제
        http.formLogin(FormLoginConfigurer::disable);

        // 6. 로그인 인증창이 뜨지 않게 비활성화
        http.httpBasic(HttpBasicConfigurer::disable);

        // 7. 커스텀 필터 적용
        http.with(new CustomSecurityFilterManager(), CustomSecurityFilterManager::build);

        // 8. 인증 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                    filterResponse.writeResponse(response, new UnauthorizedException(BaseException.USER_UNAUTHORIZED));
                })
        );

        // 9. 권한 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                    filterResponse.writeResponse(response, new ForbiddenException(BaseException.AUTH_PERMISSION_DENIED));
                })
        );

        // 11. 인증, 권한 필터 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/alert/**")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/hand-off/**")
                        ).hasAuthority("user")
                        .requestMatchers(
                                new AntPathRequestMatcher("/company/open-api/**"),
                                new AntPathRequestMatcher("/backoffice/**")
                        ).hasAuthority("super_admin")
                        .requestMatchers(
                                new AntPathRequestMatcher("/company/db/**")
                        ).hasAuthority("admin")
                        .requestMatchers(
                                new AntPathRequestMatcher("/monitoring/**"),
                                new AntPathRequestMatcher("/router/**"),
                                new AntPathRequestMatcher("/profile/**"),
                                new AntPathRequestMatcher("/user/lock/**")
                        ).hasAnyAuthority("admin", "super_admin")
                        .requestMatchers(
                                new AntPathRequestMatcher("/user/logout"),
                                new AntPathRequestMatcher("/user/withdraw")
                        ).authenticated()
                        .anyRequest().permitAll()
        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("http://localhost:8080");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
