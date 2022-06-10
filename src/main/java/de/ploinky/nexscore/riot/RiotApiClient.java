package de.ploinky.nexscore.riot;

import de.ploinky.nexscore.exception.ExternalApiErrorException;
import de.ploinky.nexscore.exception.MatchDoesNotExistException;
import de.ploinky.nexscore.exception.SummonerDoesNotExistException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class RiotApiClient {
    private static final Logger log = LoggerFactory.getLogger(RiotApiClient.class);

    @Autowired
    private WebClient webClient;

    @Autowired
    private WebClient webClientEurope;

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

    public Optional<RiotApiClientPlayer> getPlayerByPuuid(String puuid) {
        return webClient.get()
                .uri("/lol/summoner/v4/summoners/by-puuid/{puuid}?api_key={riotApiKey}",
                        puuid, riotApiKey)
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

    public Optional<List<String>> getMatchIdsByPuuid(String puuid) {
        return webClientEurope.get()
                .uri("/lol/match/v5/matches/by-puuid/{puuid}/ids?api_key={riotApiKey}",
                        puuid, riotApiKey)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
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

    public Optional<RiotApiClientMatch> getMatchByMatchId(String matchId) {
        return webClientEurope.get()
                .uri("/lol/match/v5/matches/{matchId}?api_key={riotApiKey}",
                        matchId, riotApiKey)
                .retrieve()
                .bodyToMono(RiotApiClientMatch.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                    HttpStatus status = e.getStatusCode();
                    if (status == HttpStatus.valueOf(404)) {
                        return new MatchDoesNotExistException();
                    } else {
                        return new ExternalApiErrorException();
                    }
                })
                .blockOptional();
    }
}
