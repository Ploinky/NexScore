package de.ploinky.nexscore.controller;

import de.ploinky.nexscore.model.Player;
import de.ploinky.nexscore.service.PlayerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/player")
    public Player postPlayer(@RequestParam(value = "name") String name) {
        return playerService.createPlayer(name);
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return playerService.getPlayers();
    }
}