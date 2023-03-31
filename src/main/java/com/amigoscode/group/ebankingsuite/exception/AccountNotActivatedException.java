package com.amigoscode.group.ebankingsuite.exception;

public class AccountNotActivatedException extends RuntimeException{
    public AccountNotActivatedException(String message) {
        super(message);
    }
}
