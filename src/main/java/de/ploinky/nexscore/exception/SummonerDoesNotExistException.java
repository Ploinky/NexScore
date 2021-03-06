package de.ploinky.nexscore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Summoner does not exist")
public class SummonerDoesNotExistException extends RuntimeException {
}
