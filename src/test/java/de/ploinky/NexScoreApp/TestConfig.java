package de.ploinky.NexScoreApp;

import de.ploinky.NexScoreApp.controller.PlayerControllerTest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@TestConfiguration
public class TestConfig {
    @Bean(name = "webClient")
    WebClient test() throws IOException {
        PlayerControllerTest.mockServer = new MockWebServer();
        PlayerControllerTest.mockServer.start();
        HttpUrl url = PlayerControllerTest.mockServer.url("/");
        WebClient webClient = WebClient.create(url.toString());
        return webClient;
    }
}
