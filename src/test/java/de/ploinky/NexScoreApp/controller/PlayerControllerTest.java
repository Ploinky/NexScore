package de.ploinky.NexScoreApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ploinky.NexScoreApp.DbIntegrationTest;
import de.ploinky.NexScoreApp.model.Player;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest extends DbIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPlayerPost() throws Exception {
        Player player = new Player("PlayerName");
        final String expectedResponseContent = objectMapper.writeValueAsString(player);

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

        mockMvc.perform(post("/player?name=" + player.getName()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/player?name=" + player.getName()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Player already exists"));
    }
}