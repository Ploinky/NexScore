package de.ploinky.NexScoreApp.service;

import de.ploinky.NexScoreApp.exception.PlayerCreationFailedDuplicateException;
import de.ploinky.NexScoreApp.model.Player;
import de.ploinky.NexScoreApp.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public void createPlayer(final Player player) {
        if(playerRepository.existsById(player.getName())) {
            throw new PlayerCreationFailedDuplicateException();
        }

        playerRepository.save(player);
    }
}
