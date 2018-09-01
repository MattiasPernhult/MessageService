package app.service.jwt;

import app.http.exception.ApiException;

import java.util.UUID;

public class MockJwt implements Jwt {

    private boolean throwForGenerateToken;
    private boolean generateInvalidToken;

    public MockJwt(boolean throwForGenerateToken, boolean generateInvalidToken) {
        this.throwForGenerateToken = throwForGenerateToken;
        this.generateInvalidToken = generateInvalidToken;
    }

    @Override
    public String generateToken(ClaimData claimData) throws ApiException {
        if (throwForGenerateToken) {
            throw ApiException.ServerError;
        }

        if (generateInvalidToken) {
            return UUID.randomUUID().toString();
        }

        return null;
    }

    @Override
    public ClaimData verifyToken(String jwtToken) throws ApiException {
        return null;
    }
}
