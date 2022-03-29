package de.ploinky.NexScoreApp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ploinky.NexScoreApp.model.Player;

@RestController
public class PlayerController {
        @PostMapping("/player")
        public Player postPlayer(@RequestParam(value="name") String name) {
            return new Player(name);
        }
}
