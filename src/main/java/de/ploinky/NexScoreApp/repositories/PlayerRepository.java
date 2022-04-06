package de.ploinky.NexScoreApp.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.ploinky.NexScoreApp.model.Player;

@EnableScan
public interface PlayerRepository  extends CrudRepository<Player, String> {
}
