package de.ploinky.nexscore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No matchId provided")
public class MatchCreationFailedNoMatchIdException extends RuntimeException {
}
