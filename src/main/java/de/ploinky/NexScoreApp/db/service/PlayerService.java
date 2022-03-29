package de.ploinky.NexScoreApp.db.service;

import de.ploinky.NexScoreApp.db.repositories.PlayerRepository;
import de.ploinky.NexScoreApp.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public void createPlayer(final Player player) {
        playerRepository.save(player);
    }
}
