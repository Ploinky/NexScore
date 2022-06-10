package de.ploinky.nexscore.service;

import de.ploinky.nexscore.model.Player;
import de.ploinky.nexscore.repository.MatchRepository;
import de.ploinky.nexscore.riot.RiotApiClient;
import de.ploinky.nexscore.riot.RiotApiClientPlayer;
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

    @Autowired
    private MatchRepository matchRepository;

    @Scheduled(cron = "5 * * * * *")
    public void readData() {
        playerService.getPlayers()
            .stream()
            .findFirst()
            .ifPresent(this::updatePlayer);
    }

    private void updatePlayerData(Player player) {
        updatePlayer(player);
        updatePlayerMatches(player);
    }

    private void updatePlayer(Player player) {
        riotApiClient.getPlayerByPuuid(player.getPuuid())
            .ifPresent(p -> {
                player.setName(p.getName());
                playerService.updatePlayer(player);
            });
    }

    private void updatePlayerMatches(Player player) {
        riotApiClient.getMatchIdsByPuuid(player.getPuuid())
                .ifPresent(matchIds -> {
                    matchIds.stream()
                        .filter(matchId -> !matchRepository.existsById(matchId))
                        .forEach(matchId -> {
                            matchService.createMatch(matchId);
                        });
                });
    }
}
