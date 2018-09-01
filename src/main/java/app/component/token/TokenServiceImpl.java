package app.component.token;

import app.component.user.User;
import app.component.user.UserRepository;
import app.http.exception.ApiException;
import app.service.jwt.ClaimData;
import app.service.jwt.Jwt;

public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private Jwt jwt;

    TokenServiceImpl(TokenRepository tokenRepository, UserRepository userRepository, Jwt jwt) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.jwt = jwt;
    }

    @Override
    public void create(Token token) throws ApiException {
        User user = this.userRepository.findUserById(token.getUser().getId());
        if (user == null) {
            throw ApiException.UserNotFound;
        }
        token.setUser(user);

        ClaimData claimData = new ClaimData(token.getUser().getId());
        String jwtToken = this.jwt.generateToken(claimData);
        token.setJwtToken(jwtToken);

        this.tokenRepository.createToken(token);
    }

    @Override
    public Token verify(String jwtToken, String userId) throws ApiException {
        Token token = this.tokenRepository.findTokenByJwtToken(jwtToken);
        if (token == null) {
            throw ApiException.TokenNotFound;
        }

        if (!token.getUser().getId().equals(userId)) {
            throw ApiException.InvalidToken;
        }

        ClaimData claimData = this.jwt.verifyToken(jwtToken);
        if (!claimData.getUserId().equals(userId)) {
            throw ApiException.InvalidToken;
        }

        User user = this.userRepository.findUserById(token.getUser().getId());
        if (user == null) {
            throw ApiException.UserNotFound;
        }
        token.setUser(user);

        return token;
    }
}
