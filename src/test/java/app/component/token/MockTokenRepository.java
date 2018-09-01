package app.component.token;

import app.http.exception.ApiException;

public class MockTokenRepository implements TokenRepository {

    private TokenRepository realRepo;
    private boolean failOnCreate;

    MockTokenRepository(TokenRepository realRepo) {
        this.realRepo = realRepo;
    }

    MockTokenRepository failOnCreate() {
        this.failOnCreate = true;
        return this;
    }

    @Override
    public void createToken(Token token) throws ApiException {
        if (failOnCreate) {
            throw ApiException.ServerError;
        }

        this.realRepo.createToken(token);
    }

    @Override
    public Token findTokenByJwtToken(String jwtToken) throws ApiException {
        return this.realRepo.findTokenByJwtToken(jwtToken);
    }
}
