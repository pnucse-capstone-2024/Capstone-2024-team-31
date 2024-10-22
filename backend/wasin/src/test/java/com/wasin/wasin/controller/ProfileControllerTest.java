package com.wasin.wasin.controller;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.util.SshConnectionUtil;
import com.wasin.wasin.util.TestModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("프로파일 통합 테스트")
public class ProfileControllerTest extends TestModule {

    @MockBean
    private SshConnectionUtil sshConnectionUtil;

    @Nested
    @DisplayName("프로파일 전체 조회")
    class FindAllTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        public void success() throws Exception {
            // given
            given(sshConnectionUtil.connect(any(), any())).willReturn("");

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.get("/profile")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
            result.andExpect(jsonPath("$.response.profiles").isArray());
            result.andExpect(jsonPath("$.response.profiles[0].profileId").isNumber());
        }

        @DisplayName("실패 - 일반 사용자 접근 금지")
        @Test
        @WithUserDetails(USER_ACTIVE_EMAIL)
        public void fail() throws Exception {
            // given
            given(sshConnectionUtil.connect(any(), any())).willReturn("");

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.get("/profile")
            );
            logResult(result);

            // then
            BaseException exception = BaseException.AUTH_PERMISSION_DENIED;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));

        }
    }

    @Nested
    @DisplayName("프로파일 모드 자동으로 변경")
    class ChangeModeAutoTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0918@naver.com")
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/profile/mode/auto")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 이미 자동화된 유저")
        @Test
        @WithUserDetails("leena0912@naver.com")
        public void fail() throws Exception {
            // given

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/profile/mode/auto")
            );
            logResult(result);

            // then
            BaseException exception = BaseException.PROFILE_ALREADY_AUTO;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));

        }
    }

    @Nested
    @DisplayName("프로파일 수동 자동으로 변경")
    class ChangeModeManualTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0912@naver.com")
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/profile/mode/manual")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 이미 자동화된 유저")
        @Test
        @WithUserDetails("leena0918@naver.com")
        public void fail() throws Exception {
            // given

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/profile/mode/manual")
            );
            logResult(result);

            // then
            BaseException exception = BaseException.PROFILE_ALREADY_MANUAL;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));

        }
    }

    @Nested
    @DisplayName("프로파일 변경")
    class ChangeProfileTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0918@naver.com")
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/profile/2")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 이미 자동화된 유저")
        @Test
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        public void fail() throws Exception {
            // given

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/profile/100")
            );
            logResult(result);

            // then
            BaseException exception = BaseException.PROFILE_NOT_FOUND;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));

        }
    }

}
