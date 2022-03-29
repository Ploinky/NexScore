package de.ploinky.NexScoreApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ploinky.NexScoreApp.db.service.PlayerService;
import de.ploinky.NexScoreApp.model.Player;

@SpringBootTest
public class DynamoDBTest {
    @Autowired
    private PlayerService playerService;

    @Test
    void testCreatePlayer() {
        Player player = new Player();
        player.setName("PlayerName");
        player.setPuuid("puuid001");
        playerService.createPlayer(player);
    }
}
