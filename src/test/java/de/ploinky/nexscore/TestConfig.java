package de.ploinky.nexscore;

import de.ploinky.nexscore.controller.PlayerControllerTest;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

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
