package de.ploinky.NexScoreApp.controller;

import de.ploinky.NexScoreApp.model.APIDescription;
import de.ploinky.NexScoreApp.model.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
        @PostMapping("/player")
        public Player postPlayer(@RequestParam(value="name") String name) {
            return new Player(name);
        }
}
