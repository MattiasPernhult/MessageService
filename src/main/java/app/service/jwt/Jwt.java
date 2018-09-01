package app.service.jwt;

import app.http.exception.ApiException;

public interface Jwt {
    String generateToken(ClaimData claimData) throws ApiException;

    ClaimData verifyToken(String jwtToken) throws ApiException;
}
