package de.ploinky.nexscore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Match already exists")
public class MatchCreationFailedDuplicateException extends RuntimeException {
}
