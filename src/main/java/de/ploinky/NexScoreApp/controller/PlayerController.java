package de.ploinky.NexScoreApp.controller;

import de.ploinky.NexScoreApp.exception.PlayerCreationFailedException;
import de.ploinky.NexScoreApp.riot.RiotApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ploinky.NexScoreApp.model.Player;
import de.ploinky.NexScoreApp.service.PlayerService;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private RiotApiClient riotApiClient;

    @PostMapping("/player")
    public Player postPlayer(@RequestParam(value="name") String name) {
        Player player = new Player();
        player.setName(name);
        playerService.createPlayer(player);

        Player rp = riotApiClient.getPlayerBySummonerName(name)
                .orElseThrow(() -> new PlayerCreationFailedException());

        return rp;
    }
}