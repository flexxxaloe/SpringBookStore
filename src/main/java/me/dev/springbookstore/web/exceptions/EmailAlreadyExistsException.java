package me.dev.springbookstore.web.exceptions;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
