package de.ploinky.nexscore.service;

import de.ploinky.nexscore.model.Match;
import de.ploinky.nexscore.repository.MatchRepository;
import de.ploinky.nexscore.riot.RiotApiClient;
import de.ploinky.nexscore.riot.RiotApiClientMatch;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    private static final Logger log = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RiotApiClient riotApiClient;

    public Match createMatch(String matchId) {
        if (Objects.isNull(matchId) || matchId.isBlank()) {
            // TODO: 20.05.22 Replace with custom exception
            throw new RuntimeException();
        }

        if (matchRepository.existsById(matchId)) {
            // TODO: 20.05.22 Replace with custom exception
            throw new RuntimeException();

        }

        log.info(matchId);

        // TODO: 20.05.22 Fetch match from Riot API
        // TODO: 23.05.22 Use custom exception
        RiotApiClientMatch riotMatch = riotApiClient.getMatchByMatchId(matchId).orElseThrow(() ->
                new RuntimeException());

        Match match = new Match(riotMatch.getMetadata().matchId);
        matchRepository.save(match);

        return match;
    }
}
