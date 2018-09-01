package app.service.jwt;

import app.component.token.Token;
import app.http.exception.ApiException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;

import java.util.Map;

public class JwtHS256 implements Jwt {

    private final String USER_ID_CLAIM = "user_id";

    private String issuer;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    public JwtHS256(String issuer, String secret) {
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(this.algorithm).
                withIssuer(issuer).
                acceptLeeway(1).
                acceptExpiresAt(5).
                build();
    }

    public synchronized String generateToken(ClaimData claimData) throws ApiException {
        try {
            DateTime datetime = DateTime.
                    now().
                    plusSeconds(Token.EXPIRES_AFTER_SECONDS);

            return JWT.create().
                    withIssuer(this.issuer).
                    withExpiresAt(datetime.toDate()).
                    withClaim(USER_ID_CLAIM, claimData.getUserId()).
                    sign(this.algorithm);

        } catch (Exception e) {
            throw ApiException.ServerError.setDetail(e.getMessage());
        }
    }

    public synchronized ClaimData verifyToken(String jwtToken) throws ApiException {
        try {
            DecodedJWT jwt = this.verifier.verify(jwtToken);
            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get(USER_ID_CLAIM);

            return new ClaimData(claim.asString());

        } catch (JWTVerificationException e) {
            throw ApiException.InvalidToken.setDetail(e.getMessage());
        }
    }
}
