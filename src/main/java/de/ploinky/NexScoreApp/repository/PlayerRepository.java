package de.ploinky.NexScoreApp.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import de.ploinky.NexScoreApp.model.Player;

import java.util.List;

@EnableScan
public interface PlayerRepository  extends CrudRepository<Player, String> {
    List<Player> findAll();
}
