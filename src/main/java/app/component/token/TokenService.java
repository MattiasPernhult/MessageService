package app.component.token;

import app.http.exception.ApiException;

public interface TokenService {
    void create(Token token) throws ApiException;

    Token verify(String jwtToken) throws ApiException;
}
