package com.example.securitytest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // 스프링 부트가 MockMvc를 자동 구성하게 합니다.
public class MainTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증되지 않은 사용자는 '/hello' 를 요청할 수 없다.")
    void hellUnauthenticated() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 '/hello' 를 요청할 수 있다.")
    void helloAuthenticated() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(content().string("Hello!"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mary", password = "12345", authorities = "read")
    @DisplayName("MockUser 세부 정보 설정")
    void helloAuthenticated2() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(content().string("Hello!"))
                .andExpect(status().isOk());
    }

    @Test
    void helloAuthenticatedWithUser() throws Exception {
        mockMvc.perform(
                        get("/hello").with(user("mary"))
                )
                .andExpect(content().string("Hello!"))
                .andExpect(status().isOk());
    }

    // UserDetailsService에서 john을 로드한다.
    // 이 경우 UserDetailsService 빈이 있어야 합니다.
    @Test
    @WithUserDetails("mary")
    void helloAuthenticatedWithUserDetails() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(content().string("Hello!"))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomUser(username = "mary")
    void helloAuthenticationWithCustomSecurityContextFactory() throws Exception {
        // 인증 논리를 건너뛰기 떄문에 권한 부여 및 인증 이후 처리 작업만 테스트 가능합니다.
        // 특정 사용자에 대한 테스트는 불가능
        mockMvc.perform(get("/hello"))
                .andExpect(content().string("Hello!"))
                .andExpect(status().isOk());
    }

}
