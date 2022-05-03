package de.ploinky.nexscore.repository;

import de.ploinky.nexscore.model.Player;
import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface PlayerRepository  extends CrudRepository<Player, String> {
    List<Player> findAll();
}
