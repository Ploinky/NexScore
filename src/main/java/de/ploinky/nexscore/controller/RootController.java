package de.ploinky.nexscore.controller;

import de.ploinky.nexscore.model.APIDescription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public APIDescription root() {
        return new APIDescription();
    }
}
