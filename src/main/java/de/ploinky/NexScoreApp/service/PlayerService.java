package de.ploinky.NexScoreApp.service;

import de.ploinky.NexScoreApp.exception.PlayerCreationFailedDuplicateException;
import de.ploinky.NexScoreApp.exception.ExternalAPIErrorException;
import de.ploinky.NexScoreApp.model.Player;
import de.ploinky.NexScoreApp.repository.PlayerRepository;

import de.ploinky.NexScoreApp.riot.RiotApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RiotApiClient riotApiClient;

    public Player createPlayer(String name) {
        if(playerRepository.existsById(name)) {
            throw new PlayerCreationFailedDuplicateException();
        }

        Player rp = riotApiClient.getPlayerBySummonerName(name)
                .orElseThrow(() -> new ExternalAPIErrorException());

        playerRepository.save(rp);

        return rp;
    }
}
