package de.ploinky.NexScoreApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RiotApiClientConfig {
    @Bean(name="webClient")
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://euw1.api.riotgames.com")
                .build();
    }
}
