package com.wasin.wasin.controller;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.domain.entity.Email;
import com.wasin.wasin.domain.entity.Token;
import com.wasin.wasin.repository.MailRepository;
import com.wasin.wasin.repository.TokenRepository;
import com.wasin.wasin.util.TestModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.wasin.wasin.dummy.DummyEntity.getTestToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@DisplayName("유저 통합 테스트")
public class UserControllerTest extends TestModule {

    @MockBean
    private MailRepository mailRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            // given
            String email = nonExistEmail;
            UserRequest.SignUpDTO requestDTO = new UserRequest.SignUpDTO(
                    "user",
                    "username",
                    email,
                    "qwer1234!",
                    "qwer1234!"
            );
            String requestBody = om.writeValueAsString(requestDTO);
            Email expectedEmail = Email.builder().isVerified(true).build();
            given(mailRepository.findById(email)).willReturn(Optional.ofNullable(expectedEmail));

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/signup")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 잘못된 역할 이름")
        @Test
        public void fail_wrong_role() throws Exception {
            // given
            String email = nonExistEmail;
            UserRequest.SignUpDTO requestDTO = new UserRequest.SignUpDTO(
                    "user_is_good",
                    "username",
                    email,
                    "qwer1234!",
                    "qwer1234!"
            );
            String requestBody = om.writeValueAsString(requestDTO);
            Email expectedEmail = Email.builder().isVerified(true).build();
            given(mailRepository.findById(email)).willReturn(Optional.ofNullable(expectedEmail));

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/signup")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            BaseException expect = BaseException.USER_ROLE_WRONG;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(expect.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(expect.getMessage()));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            // given
            String email = SUPER_ADMIN_EMAIL;
            String password = "password1@";
            UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO(email, password, "fcm-token");
            String requestBody = om.writeValueAsString(requestDTO);

            Email expectedEmail = Email.builder().isVerified(true).build();
            given(mailRepository.findById(email)).willReturn(Optional.ofNullable(expectedEmail));

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/login")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 잘못된 패스워드")
        @Test
        public void fail_wrong_role() throws Exception {
            // given
            String email = SUPER_ADMIN_EMAIL;
            String password = "password123@";
            UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO(email, password, "fcm-token");
            String requestBody = om.writeValueAsString(requestDTO);

            Email expectedEmail = Email.builder().isVerified(true).build();
            given(mailRepository.findById(email)).willReturn(Optional.ofNullable(expectedEmail));

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/login")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            BaseException expect = BaseException.USER_PASSWORD_WRONG;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(expect.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(expect.getMessage()));
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class LogoutTest {

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            // given
            String email = SUPER_ADMIN_EMAIL;
            UserResponse.Token tokenResult = getToken(email);
            Token token = getTestToken(tokenResult, email);

            given(tokenRepository.findByAccessToken(tokenResult.accessToken())).willReturn(Optional.ofNullable(token));
            willDoNothing().given(tokenRepository).delete(any());

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/logout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(jwtProvider.AUTHORIZATION_HEADER, tokenResult.accessToken())
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 존재하지 않는 토큰")
        @Test
        public void fail() throws Exception {
            // given
            String email = SUPER_ADMIN_EMAIL;
            UserResponse.Token tokenResult = getToken(email);

            given(tokenRepository.findByAccessToken(tokenResult.accessToken())).willReturn(Optional.empty());

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/logout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(jwtProvider.AUTHORIZATION_HEADER, tokenResult.accessToken())
            );
            logResult(result);

            // then
            BaseException expect = BaseException.ACCESS_TOKEN_NOT_FOUND;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(expect.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(expect.getMessage()));
        }
    }

    @Nested
    @DisplayName("회원탈퇴 테스트")
    class WithdrawTest {

        @WithUserDetails(ADMIN_ACTIVE_EMAIL)
        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.delete("/user/withdraw")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

    }

    @Nested
    @DisplayName("토큰 리프레시 테스트")
    class TokenRefreshTest {

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            // given
            String email = SUPER_ADMIN_EMAIL;
            UserResponse.Token tokenDTO = getToken(email);
            Token token = getTestToken(tokenDTO, email);
            String requestBody = om.writeValueAsString(tokenDTO);

            given(tokenRepository.existsById(token.getRefreshToken())).willReturn(true);
            willDoNothing().given(tokenRepository).deleteById(token.getRefreshToken());

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/auth/reissue")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
            result.andExpect(jsonPath("$.response.accessToken").isString());
            result.andExpect(jsonPath("$.response.refreshToken").isString());
            result.andExpect(jsonPath("$.error").doesNotExist());
        }

        @DisplayName("실패 - 존재하지 않는 유저")
        @Test
        public void fail() throws Exception {
            String email = SUPER_ADMIN_EMAIL;
            UserResponse.Token tokenDTO = getToken(email);
            String requestBody = om.writeValueAsString(tokenDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/auth/reissue")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );

            // then
            BaseException expect = BaseException.REFRESH_TOKEN_NOT_FOUND;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(expect.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(expect.getMessage()));
        }
    }

    @Nested
    @DisplayName("잠금해제 패스워드 설정 테스트")
    class UnlockPasswordTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        public void success() throws Exception {
            // given
            UserRequest.LockDTO requestDTO = new UserRequest.LockDTO("1234");
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/lock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 5자리의 잠금해제 패스워드 입력")
        @Test
        public void fail() throws Exception {
            // given
            UserRequest.LockDTO requestDTO = new UserRequest.LockDTO("12334");
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/lock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("false"));
        }
    }

    @Nested
    @DisplayName("잠금해제 패스워드 확인 테스트")
    class UnlockPasswordConfirmTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        public void success() throws Exception {
            // given
            UserRequest.LockDTO requestDTO = new UserRequest.LockDTO("6666");
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/lock/confirm")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 틀린 잠금해제 패스워드 입력")
        @Test
        public void fail() throws Exception {
            // given
            UserRequest.LockDTO requestDTO = new UserRequest.LockDTO("1299");
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/lock/confirm")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("false"));
        }
    }

    @Nested
    @DisplayName("이메일 인증 코드 전송")
    class EmailSendTest {

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            String email = nonExistEmail;
            UserRequest.EmailDTO requestDTO = new UserRequest.EmailDTO(email);
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/auth/email")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 동일한 이메일 존재")
        @Test
        public void fail() throws Exception {
            String email = SUPER_ADMIN_EMAIL;
            UserRequest.EmailDTO requestDTO = new UserRequest.EmailDTO(email);
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/auth/email")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            BaseException expect = BaseException.USER_EMAIL_EXIST;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(expect.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(expect.getMessage()));
        }
    }

    @Nested
    @DisplayName("이메일 인증 코드 확인")
    class EmailCheckTest {

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            String email = nonExistEmail;
            String code = "123456";
            UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO(email, code);
            String requestBody = om.writeValueAsString(requestDTO);

            Email mail = Email.builder().email(email).code(code).isVerified(false).build();
            given(mailRepository.findById(email)).willReturn(Optional.ofNullable(mail));

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/auth/email")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 이미 존재하는 이메일")
        @Test
        public void fail() throws Exception {
            String email = SUPER_ADMIN_EMAIL;
            String code = "123456";
            UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO(email, code);
            String requestBody = om.writeValueAsString(requestDTO);

            Email mail = Email.builder().email(email).code(code).isVerified(false).build();
            given(mailRepository.findById(email)).willReturn(Optional.ofNullable(mail));

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/user/auth/email")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            logResult(result);

            // then
            BaseException expect = BaseException.USER_EMAIL_EXIST;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(expect.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(expect.getMessage()));
        }
    }

}
