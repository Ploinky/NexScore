package de.ploinky.NexScoreApp.service;

import de.ploinky.NexScoreApp.exception.PlayerCreationFailedDuplicateException;
import de.ploinky.NexScoreApp.exception.ExternalAPIErrorException;
import de.ploinky.NexScoreApp.exception.PlayerCreationFailedNoNameException;
import de.ploinky.NexScoreApp.model.Player;
import de.ploinky.NexScoreApp.repository.PlayerRepository;

import de.ploinky.NexScoreApp.riot.RiotApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RiotApiClient riotApiClient;

    public Player createPlayer(String name) {
        if(Objects.isNull(name) || name.isBlank()) {
            throw new PlayerCreationFailedNoNameException();
        }

        if(playerRepository.existsById(name)) {
            throw new PlayerCreationFailedDuplicateException();
        }

        Player player = riotApiClient.getPlayerBySummonerName(name)
                .map(rp -> {
                    Player p = new Player(rp.getName());
                    p.setPuuid(rp.getPuuid());
                    return p;
                })
                .orElseThrow(() -> new ExternalAPIErrorException());

        playerRepository.save(player);

        return player;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }
}
