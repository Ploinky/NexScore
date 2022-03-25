package de.ploinky.NexScoreApp.controller;

import de.ploinky.NexScoreApp.model.Ping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/ping")
    public Ping greeting(@RequestParam(value="greeting", defaultValue="Hello") String greeting) {
        return new Ping(greeting + ", World!");
    }
}
