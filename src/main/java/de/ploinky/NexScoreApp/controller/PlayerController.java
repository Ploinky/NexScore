package de.ploinky.NexScoreApp.controller;

import de.ploinky.NexScoreApp.db.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ploinky.NexScoreApp.model.Player;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/player")
    public Player postPlayer(@RequestParam(value="name") String name) {
        Player player = new Player();
        player.setName(name);
        player.setPuuid("puuid001");
        playerService.createPlayer(player);
        return player;
    }
}