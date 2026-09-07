package me.dev.springbookstore.web.exceptions;

public class UsernameAlreadyExistsException extends AlreadyExistsException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
