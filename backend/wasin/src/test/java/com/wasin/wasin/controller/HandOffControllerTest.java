package com.wasin.wasin.controller;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin.domain.dto.HandOffRequest;
import com.wasin.wasin.util.TestModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("핸드오프 통합 테스트")
public class HandOffControllerTest extends TestModule {

    @Nested
    @DisplayName("사용자 라우터 전체 리스트 조회")
    class FindAll {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0917@naver.com")
        public void success() throws Exception {
            // given
            HandOffRequest.UserRouterDTO requestDTO = new HandOffRequest.UserRouterDTO(
                List.of(
                    new HandOffRequest.RouterDTO("휴게실 wifi", "5a:86:94:7f:b4:c7", 2L, -60L),
                    new HandOffRequest.RouterDTO("없는 wifi", "5c:90:94:7f:b4:c7", 3L, -60L)
                )
            );
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/hand-off")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
            result.andExpect(jsonPath("$.response.isAuto").value("true"));
            result.andExpect(jsonPath("$.response.routerList[0].isSystemExist").value("true"));
            result.andExpect(jsonPath("$.response.routerList[1].isSystemExist").value("false"));
        }

        @DisplayName("실패")
        @Test
        @WithUserDetails("leena0917@naver.com")
        public void fail() throws Exception {
            // given
            HandOffRequest.UserRouterDTO requestDTO = new HandOffRequest.UserRouterDTO(List.of());
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/hand-off")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(jsonPath("$.success").value("false"));
        }
    }

    @Nested
    @DisplayName("사용자 최적 라우터 조회")
    class FindBestRouter {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0917@naver.com")
        public void success() throws Exception {
            // given
            HandOffRequest.UserRouterDTO requestDTO = new HandOffRequest.UserRouterDTO(
                    List.of(
                            new HandOffRequest.RouterDTO("휴게실 wifi", "5a:86:94:7f:b4:c7", 2L, -60L),
                            new HandOffRequest.RouterDTO("없는 wifi", "5c:90:94:7f:b4:c7", 3L, -60L)
                    )
            );
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/hand-off/best")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
            result.andExpect(jsonPath("$.response.isAuto").value("true"));
            result.andExpect(jsonPath("$.response.router.isSystemExist").value("true"));
        }

        @DisplayName("실패")
        @Test
        @WithUserDetails("leena0917@naver.com")
        public void fail() throws Exception {
            // given
            HandOffRequest.UserRouterDTO requestDTO = new HandOffRequest.UserRouterDTO(List.of(
                    new HandOffRequest.RouterDTO("", "", -60L, -60L)
            ));
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/hand-off/best")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(jsonPath("$.success").value("false"));
        }
    }

    @Nested
    @DisplayName("핸드오프 모드 자동으로 변경")
    class ChangeModeAutoTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0920@naver.com")
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/hand-off/mode/auto")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 이미 자동화된 유저")
        @Test
        @WithUserDetails("leena0917@naver.com")
        public void fail() throws Exception {
            // given

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/hand-off/mode/auto")
            );
            logResult(result);

            // then
            BaseException exception = BaseException.HANDOFF_ALREADY_AUTO;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));

        }
    }

    @Nested
    @DisplayName("핸드오프 모드 수동으로 변경")
    class ChangeModeManualTest {

        @DisplayName("성공")
        @Test
        @WithUserDetails("leena0917@naver.com")
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/hand-off/mode/manual")
            );
            logResult(result);

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 이미 수동화된 유저")
        @Test
        @WithUserDetails("leena0920@naver.com")
        public void fail() throws Exception {
            // given

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/hand-off/mode/manual")
            );
            logResult(result);

            // then
            BaseException exception = BaseException.HANDOFF_ALREADY_MANUAL;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));

        }
    }
}
