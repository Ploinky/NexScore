package de.ploinky.nexscore.controller;

import de.ploinky.nexscore.model.ApiDescription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public ApiDescription root() {
        return new ApiDescription();
    }
}
