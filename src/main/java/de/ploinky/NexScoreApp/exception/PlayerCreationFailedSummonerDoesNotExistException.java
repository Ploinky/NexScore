package de.ploinky.NexScoreApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Summoner does not exist")
public class PlayerCreationFailedSummonerDoesNotExistException extends RuntimeException {
}
