package com.spring.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceException extends DuplicateKeyException {
    public DuplicateResourceException(String emailAlreadyTaken) {
        super(emailAlreadyTaken);
    }
}
