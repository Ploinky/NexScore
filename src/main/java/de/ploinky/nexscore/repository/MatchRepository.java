package de.ploinky.nexscore.repository;

import de.ploinky.nexscore.model.Match;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, String> {
}
