package de.ploinky.NexScoreApp.riot;

import de.ploinky.NexScoreApp.exception.PlayerCreationFailedException;
import de.ploinky.NexScoreApp.exception.PlayerCreationFailedSummonerDoesNotExistException;
import de.ploinky.NexScoreApp.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class RiotApiClient {
    private final WebClient webClient;

    @Value("${riot.api.key}")
    private String riotApiKey;

    @Autowired
    public RiotApiClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://euw1.api.riotgames.com")
                .build();
    }

    public Optional<Player> getPlayerBySummonerName(String name) {
        return webClient.get().uri("/lol/summoner/v4/summoners/by-name/{name}?api_key={riotApiKey}", name, riotApiKey)
                .retrieve()
                .bodyToMono(Player.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                        HttpStatus status = e.getStatusCode();
                        if(status == HttpStatus.valueOf(404)) {
                            return new PlayerCreationFailedSummonerDoesNotExistException();
                        } else {
                            return new PlayerCreationFailedException();
                        }
                })
                .blockOptional();
    }
}
