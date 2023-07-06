package com.example.chi_academy_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Allow the controllers to return a 403 if an operation is forbidden.
*/
@ResponseStatus(HttpStatus.FORBIDDEN)
public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
