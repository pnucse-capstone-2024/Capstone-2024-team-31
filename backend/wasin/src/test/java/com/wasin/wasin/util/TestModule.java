package com.wasin.wasin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wasin.wasin.config.S3MockConfig;
import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.security.JWTProvider;
import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.repository.UserJPARepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@Testcontainers
@Import(S3MockConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestModule {

    private static final Logger logger = LoggerFactory.getLogger(TestModule.class);

    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    private static final String MYSQL_IMAGE = "mysql:8";

    private static final JdbcDatabaseContainer MYSQL;
    private static final GenericContainer REDIS;

    protected static final String SUPER_ADMIN_EMAIL = "leena0912@naver.com";
    protected static final String ADMIN_ACTIVE_EMAIL = "leena0916@naver.com";
    protected static final String ADMIN_STAND_BY_EMAIL = "leena0913@naver.com";
    protected static final String USER_ACTIVE_EMAIL = "leena0917@naver.com";
    protected static final String nonExistEmail = "test0912@naver.com";


    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper om;

    @Autowired
    protected JWTProvider jwtProvider;

    @Autowired
    protected UserJPARepository userJPARepository;

    static {
        REDIS = new GenericContainer(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);
        REDIS.start();
        MYSQL = new MySQLContainer(MYSQL_IMAGE);
        MYSQL.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);

        registry.add("spring.security.jwt-config.redis.host", REDIS::getHost);
        registry.add("spring.security.jwt-config.redis.port", () -> REDIS.getMappedPort(REDIS_PORT).toString());
    }

    protected void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }

    protected UserResponse.Token getToken(String email) {
        User user = getUser(email);
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        return new UserResponse.Token(accessToken, refreshToken);
    }

    private User getUser(String email) {
        return userJPARepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND));
    }


}
