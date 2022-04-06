package de.ploinky.NexScoreApp.controller;

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

    @PostMapping("/player")
    public Player postPlayer(@RequestParam(value="name") String name) {
        Player player = new Player();
        player.setName(name);
        playerService.createPlayer(player);
        return player;
    }
}