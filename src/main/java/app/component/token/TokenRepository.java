package app.component.token;

import app.http.exception.ApiException;

public interface TokenRepository {
    void createToken(Token token) throws ApiException;

    Token findTokenByJwtToken(String jwtToken) throws ApiException;
}
