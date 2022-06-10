package de.ploinky.nexscore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class RiotApiClientConfig {
    @Bean(name = "webClient")
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://euw1.api.riotgames.com")
                .build();
    }

    @Bean(name = "webClientEurope")
    public WebClient webClientEurope(WebClient.Builder builder) {
        return builder.baseUrl("https://europe.api.riotgames.com")
                .build();
    }
}
