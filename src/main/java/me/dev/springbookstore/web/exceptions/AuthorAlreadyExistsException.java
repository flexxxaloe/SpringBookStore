package me.dev.springbookstore.web.exceptions;

public class AuthorAlreadyExistsException extends AlreadyExistsException {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
