package de.ploinky.NexScoreApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ploinky.NexScoreApp.model.Ping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PingTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGreetingDefaultMessage() throws Exception {
        Ping response = new Ping("Hello, World!");
        final String expectedResponseContent = objectMapper.writeValueAsString(response);

        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseContent));
    }

    @Test
    public void testGreetingCustomMessage() throws Exception {
        Ping response = new Ping("hi, World!");
        final String expectedResponseContent = objectMapper.writeValueAsString(response);

        mockMvc.perform(get("/ping?greeting=hi"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseContent));
    }
}