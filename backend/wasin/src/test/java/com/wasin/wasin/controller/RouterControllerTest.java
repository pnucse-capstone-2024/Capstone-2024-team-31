package com.wasin.wasin.controller;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin.domain.dto.RouterRequest;
import com.wasin.wasin.util.TestModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("라우터 통합 테스트")
public class RouterControllerTest extends TestModule {

    @Nested
    @DisplayName("전체 라우터 조회")
    class FindAllRouterTest {

        @DisplayName("성공")
        @WithUserDetails(ADMIN_ACTIVE_EMAIL)
        @Test
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.get("/router")
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
            result.andExpect(jsonPath("$.response.image.companyImage").value("https://static.wixstatic.com/media/d465da_ed07388770a5418f8db3cf4e37573c47.jpg/v1/fill/w_342,h_336,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/d465da_ed07388770a5418f8db3cf4e37573c47"));
            result.andExpect(jsonPath("$.response.image.imageHeight").value(1243));
            result.andExpect(jsonPath("$.response.image.imageWidth").value(56352));

            result.andExpect(jsonPath("$.response.routerList[0].name").value("휴게실 Wifi"));
            result.andExpect(jsonPath("$.response.routerList[0].positionX").value(124.32));
            result.andExpect(jsonPath("$.response.routerList[0].positionY").value(1653.22));
        }

        @DisplayName("실패 - 일반 사용자는 접근 불가능")
        @WithUserDetails(USER_ACTIVE_EMAIL)
        @Test
        public void fail() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.get("/router")
            );

            // then
            BaseException exception = BaseException.AUTH_PERMISSION_DENIED;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));
        }
    }

    @Nested
    @DisplayName("개별 라우터 조회")
    class FindByIdRouterTest {

        @DisplayName("성공")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.get("/router/1")
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
            result.andExpect(jsonPath("$.response.image.companyImage").value("https://static.wixstatic.com/media/d465da_ed07388770a5418f8db3cf4e37573c47.jpg/v1/fill/w_342,h_336,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/d465da_ed07388770a5418f8db3cf4e37573c47"));
            result.andExpect(jsonPath("$.response.image.imageHeight").value(1243));
            result.andExpect(jsonPath("$.response.image.imageWidth").value(56352));

            result.andExpect(jsonPath("$.response.information.name").value("휴게실 Wifi"));
            result.andExpect(jsonPath("$.response.information.ssid").value("ipTIMEOpenWrt"));
            result.andExpect(jsonPath("$.response.information.positionX").value(124.32));
            result.andExpect(jsonPath("$.response.information.positionY").value(1653.22));
        }

        @DisplayName("실패 - 대기중인 관리자는 접근 권한 없음")
        @WithUserDetails(ADMIN_STAND_BY_EMAIL)
        @Test
        public void fail() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.get("/router/1")
            );

            // then
            BaseException exception = BaseException.USER_NOT_FOUND;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));
        }
    }

    @Nested
    @DisplayName("라우터 추가")
    class CreateRouterTest {

        @DisplayName("성공")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void success() throws Exception {
            // given
            RouterRequest.CreateDTO requestDTO = new RouterRequest.CreateDTO("라우터 이름", "58:86:94:7f:b4:c4", "serialNum", "password", 8080, 1543.33, 25.34);
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/router")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - 프로메테우스에 라우터 존재하지 않음")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void fail() throws Exception {
            // given
            RouterRequest.CreateDTO requestDTO = new RouterRequest.CreateDTO("라우터 이름", "22:86:94:7f:b4:c7", "serialNum", "password", 8080, 1543.33, 25.34);
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/router")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            BaseException exception = BaseException.ROUTER_NOT_EXIST_IN_PROMETHEUS;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));
        }
    }

    @Nested
    @DisplayName("라우터 업데이트")
    class UpdateRouterTest {

        @DisplayName("성공")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void success() throws Exception {
            // given
            RouterRequest.UpdateDTO requestDTO = new RouterRequest.UpdateDTO("라우터 수정", "new password", 133.33, 25.34);
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .put("/router/1")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - DB에 라우터 존재하지 않음")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void fail() throws Exception {
            // given
            RouterRequest.UpdateDTO requestDTO = new RouterRequest.UpdateDTO("라우터 수정","new password", 133.33, 25.34);
            String requestBody = om.writeValueAsString(requestDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .put("/router/103")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            BaseException exception = BaseException.ROUTER_NOT_EXIST_IN_DB;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));
        }
    }

    @Nested
    @DisplayName("라우터 삭제")
    class DeleteRouterTest {

        @DisplayName("성공")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void success() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/router/delete/1")
            );

            // then
            result.andExpect(jsonPath("$.success").value("true"));
        }

        @DisplayName("실패 - DB에 라우터 존재하지 않음")
        @WithUserDetails(SUPER_ADMIN_EMAIL)
        @Test
        public void fail() throws Exception {
            // given

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/router/delete/103")
            );

            // then
            BaseException exception = BaseException.ROUTER_NOT_EXIST_IN_DB;
            result.andExpect(jsonPath("$.success").value("false"));
            result.andExpect(jsonPath("$.error.status").value(exception.getStatus().value()));
            result.andExpect(jsonPath("$.error.message").value(exception.getMessage()));
        }
    }

}
