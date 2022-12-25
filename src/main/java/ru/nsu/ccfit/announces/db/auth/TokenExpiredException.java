package ru.nsu.ccfit.announces.db.auth;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException() {
        super("Authentication token expired");
    }
}
