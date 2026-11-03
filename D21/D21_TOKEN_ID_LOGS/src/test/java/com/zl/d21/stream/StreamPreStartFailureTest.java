package com.zl.d21.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zl.d21.support.AbstractPersistenceCleanupTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 真实模式无效占位 Key 在 Flux 订阅前失败：必须返回 JSON 错误并把 PENDING 用户消息收口为 FAILED。
 */
@SpringBootTest(properties = "app.ai.mode=deepseek")
@AutoConfigureMockMvc
class StreamPreStartFailureTest extends AbstractPersistenceCleanupTest {

    private static final String OWNER = "ut_stream_prestart";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void credentialFailureBeforeSubscriptionShouldNotLeavePendingTurn() throws Exception {
        mockMvc.perform(post("/api/v1/ai/chat/stream")
                        .header("X-Dev-User", OWNER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("message", "订阅前配置失败测试"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(5001));

        String statusValue = jdbcTemplate.queryForObject("""
                SELECT m.status
                FROM chat_message m
                JOIN chat_session s ON s.session_id = m.session_id
                WHERE s.owner_id = ? AND m.role = 'user'
                ORDER BY m.created_at DESC LIMIT 1
                """, String.class, OWNER);
        assertThat(statusValue).isEqualTo("FAILED");
    }
}
