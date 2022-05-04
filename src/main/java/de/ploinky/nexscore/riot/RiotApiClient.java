package de.ploinky.nexscore.riot;

import de.ploinky.nexscore.exception.ExternalApiErrorException;
import de.ploinky.nexscore.exception.SummonerDoesNotExistException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class RiotApiClient {
    @Autowired
    private WebClient webClient;

    @Value("${riot.api.key}")
    private String riotApiKey;

    public Optional<RiotApiClientPlayer> getPlayerBySummonerName(String name) {
        return webClient.get()
            .uri("/lol/summoner/v4/summoners/by-name/{name}?api_key={riotApiKey}", name, riotApiKey)
            .retrieve()
            .bodyToMono(RiotApiClientPlayer.class)
            .onErrorMap(WebClientResponseException.class, e -> {
                HttpStatus status = e.getStatusCode();
                if (status == HttpStatus.valueOf(404)) {
                    return new SummonerDoesNotExistException();
                } else {
                    return new ExternalApiErrorException();
                }
            })
            .blockOptional();
    }
}