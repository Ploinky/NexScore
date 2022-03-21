package de.ploinky.NexScoreApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public APIDescription root() {
        return new APIDescription();
    }
}
