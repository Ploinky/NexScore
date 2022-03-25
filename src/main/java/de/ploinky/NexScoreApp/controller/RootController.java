package de.ploinky.NexScoreApp.controller;

import de.ploinky.NexScoreApp.model.APIDescription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public APIDescription root() {
        return new APIDescription();
    }
}
