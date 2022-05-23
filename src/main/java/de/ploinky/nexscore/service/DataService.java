package de.ploinky.nexscore.service;

import de.ploinky.nexscore.model.Player;
import de.ploinky.nexscore.riot.RiotApiClient;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private RiotApiClient riotApiClient;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MatchService matchService;

    @Scheduled(cron = "5 * * * * *")
    public void readData() {
        Player player = playerService.getPlayers().stream().findFirst().orElse(null);

        if (player == null) {
            return;
        }

        List<String> matchIds = riotApiClient.getMatchIdsByPuuid(player.getPuuid())
                .orElse(new ArrayList<>());

        if (matchIds.isEmpty()) {
            log.info("No matches found for player <" + player.getName() + ">");
            return;
        }

        matchIds.forEach(matchId -> {
            matchService.createMatch(matchId);
        });
    }
}
