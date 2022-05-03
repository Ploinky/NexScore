package de.ploinky.nexscore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Error requesting data from external API")
public class ExternalAPIErrorException extends RuntimeException {
}
