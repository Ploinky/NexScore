package de.ploinky.NexScoreApp.db.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import de.ploinky.NexScoreApp.model.Player;

@EnableScan
public interface PlayerRepository  extends CrudRepository<Player, String> {
}
