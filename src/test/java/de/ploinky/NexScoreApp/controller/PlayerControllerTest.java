package de.ploinky.NexScoreApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ploinky.NexScoreApp.DbIntegrationTest;
import de.ploinky.NexScoreApp.NexScoreAppApplication;
import de.ploinky.NexScoreApp.TestConfig;
import de.ploinky.NexScoreApp.model.Player;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
// Enable overriding webclient bean
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
// Override webclient bean with test bean
@ContextConfiguration(classes = {NexScoreAppApplication.class, TestConfig.class})
public class PlayerControllerTest extends DbIntegrationTest {
    public static MockWebServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    private void mockBackendEndpoint(int responseCode, String body) {
        MockResponse mockResponse = new MockResponse().setResponseCode(responseCode)
                .setBody(body)
                .addHeader("Content-Type", "application/json");
        mockServer.enqueue(mockResponse);
    }

    @Test
    public void testPlayerPost() throws Exception {
        Player player = new Player("Ploinky");
        player.setPuuid("HjZfChmcAk0kGs3j6_C0s1WiFU5Ypd4-T9zTfqHsGCb5z-0Hu1V0f9CAsoKsfeLoNlzqSFPgnISAmQ");
        final String expectedResponseContent = objectMapper.writeValueAsString(player);

        mockBackendEndpoint(200, expectedResponseContent);

        mockMvc.perform(post("/player?name=" + player.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseContent));
    }

    @Test
    public void testPlayerPostEmptyRequestParam() throws Exception {
        mockMvc.perform(post("/player"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required request parameter 'name' for method parameter type String is not present"));
    }

    @Test
    public void testPlayerPostDuplicateName() throws Exception {
        Player player = new Player("PlayerName1");
        final String expectedResponseContent = objectMapper.writeValueAsString(player);

        mockBackendEndpoint(200, expectedResponseContent);

        mockMvc.perform(post("/player?name=" + player.getName()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/player?name=" + player.getName()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Player already exists"));
    }

    @Test
    public void testPlayerPostPlayerDoesNotExist() throws Exception {
        Player player = new Player("1");

        mockBackendEndpoint(404, "Summoner does not exist");

        mockMvc.perform(post("/player?name=" + player.getName()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Summoner does not exist"));
    }
}