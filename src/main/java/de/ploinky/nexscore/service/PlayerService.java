package de.ploinky.nexscore.service;

import de.ploinky.nexscore.exception.PlayerCreationFailedDuplicateException;
import de.ploinky.nexscore.exception.ExternalAPIErrorException;
import de.ploinky.nexscore.exception.PlayerCreationFailedNoNameException;
import de.ploinky.nexscore.model.Player;
import de.ploinky.nexscore.repository.PlayerRepository;

import de.ploinky.nexscore.riot.RiotApiClient;
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
