package org.kosa.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFountException extends RuntimeException {
	private static final long serialVersionUID = -2547431670238811853L;

	public DataNotFountException(String message) {
		super(message);
	}
	
	
}
