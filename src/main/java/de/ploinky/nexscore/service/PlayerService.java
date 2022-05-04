package de.ploinky.nexscore.service;

import de.ploinky.nexscore.exception.ExternalApiErrorException;
import de.ploinky.nexscore.exception.PlayerCreationFailedDuplicateException;
import de.ploinky.nexscore.exception.PlayerCreationFailedNoNameException;
import de.ploinky.nexscore.model.Player;
import de.ploinky.nexscore.repository.PlayerRepository;
import de.ploinky.nexscore.riot.RiotApiClient;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RiotApiClient riotApiClient;

    public Player createPlayer(String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new PlayerCreationFailedNoNameException();
        }

        Player player = riotApiClient.getPlayerBySummonerName(name)
            .map(rp -> {
                Player p = new Player(rp.getName());
                p.setPuuid(rp.getPuuid());
                return p;
            })
            .orElseThrow(() -> new ExternalApiErrorException());

        if (playerRepository.existsById(player.getPuuid())) {
            throw new PlayerCreationFailedDuplicateException();
        }

        playerRepository.save(player);

        return player;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }
}
