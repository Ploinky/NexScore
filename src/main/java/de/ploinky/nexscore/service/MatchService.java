package de.ploinky.nexscore.service;

import de.ploinky.nexscore.exception.MatchCreationFailedDuplicateException;
import de.ploinky.nexscore.exception.MatchCreationFailedNoMatchIdException;
import de.ploinky.nexscore.exception.MatchDoesNotExistException;
import de.ploinky.nexscore.model.Match;
import de.ploinky.nexscore.riot.RiotApiClientMatch;
import de.ploinky.nexscore.repository.MatchRepository;
import de.ploinky.nexscore.riot.RiotApiClient;
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
            throw new MatchCreationFailedNoMatchIdException();
        }

        if (matchRepository.existsById(matchId)) {
            throw new MatchCreationFailedDuplicateException();
        }

        log.info(matchId);

        RiotApiClientMatch riotMatch = riotApiClient.getMatchByMatchId(matchId).orElseThrow(() ->
                new MatchDoesNotExistException());

        Match match = new Match();
        match.setMatchId(riotMatch.metadata.matchId);
        matchRepository.save(match);

        return match;
    }
}
