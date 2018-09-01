package app.component.token;

import app.component.user.MockUserRepository;
import app.component.user.User;
import app.db.MemoryDB;
import app.db.MemoryDBTestUtil;
import app.db.Type;
import app.http.exception.ApiException;
import app.service.jwt.Jwt;
import app.service.jwt.JwtHS256;
import app.service.jwt.MockJwt;
import org.junit.jupiter.api.Test;

import static app.http.exception.ApiException.*;
import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    @Test
    void testCreate_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            User user = MemoryDBTestUtil.createUser(memoryDB);

            TokenService tokenService = new TokenServiceFactory(
                    Type.MEMORY,
                    memoryDB,
                    new JwtHS256("test", "secret")
            ).create();

            Token token = new Token(user.getId());
            tokenService.create(token);

            Token storedToken = memoryDB.findTokenByJwtToken(token.getJwtToken());
            assertEquals(token, storedToken);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreate_UserNotFound() {
        try {
            TokenService tokenService = new TokenServiceFactory(
                    Type.MEMORY,
                    new MemoryDB(),
                    new JwtHS256("test", "secret")
            ).create();

            Token token = new Token("1");
            tokenService.create(token);

            ApiException e = assertThrows(ApiException.class, () -> {
                tokenService.create(token);
            });
            assertEquals(e.getMessage(), UserNotFound.getMessage());
        } catch (Exception ignored) {
        }
    }

    @Test
    void testCreate_GenerateTokenFails() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            User user = MemoryDBTestUtil.createUser(memoryDB);

            TokenService tokenService = new TokenServiceFactory(
                    Type.MEMORY,
                    memoryDB,
                    new MockJwt(true, false)
            ).create();

            Token token = new Token(user.getId());

            ApiException e = assertThrows(ApiException.class, () -> {
                tokenService.create(token);
            });
            assertEquals(e.getMessage(), ServerError.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreate_TokenCreateFails() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            User user = MemoryDBTestUtil.createUser(memoryDB);

            TokenServiceImpl tokenServiceImpl = new TokenServiceImpl(
                    new MockTokenRepository(memoryDB).failOnCreate(),
                    memoryDB,
                    new JwtHS256("test", "secret")
            );

            Token token = new Token(user.getId());
            ApiException e = assertThrows(ApiException.class, () -> {
                tokenServiceImpl.create(token);
            });
            assertEquals(e.getMessage(), ServerError.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testVerify_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Jwt jwt = new JwtHS256("test", "secret");
            Token token = MemoryDBTestUtil.createToken(memoryDB, jwt);

            TokenService tokenService = new TokenServiceFactory(
                    Type.MEMORY,
                    memoryDB,
                    jwt
            ).create();

            Token verifiedToken = tokenService.verify(token.getJwtToken());
            assertEquals(token, verifiedToken);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testVerify_TokenNotFound() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Jwt jwt = new JwtHS256("test", "secret");

            TokenService tokenService = new TokenServiceFactory(
                    Type.MEMORY,
                    memoryDB,
                    jwt
            ).create();

            ApiException e = assertThrows(ApiException.class, () -> {
                tokenService.verify("invalid-jwt-token-string");
            });
            assertEquals(e.getMessage(), TokenNotFound.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // verify fails
    @Test
    void testVerify_JwtVerificationFails() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Token token = MemoryDBTestUtil.createToken(
                    memoryDB,
                    new MockJwt(false, true)
            );

            TokenService tokenService = new TokenServiceFactory(
                    Type.MEMORY,
                    memoryDB,
                    new JwtHS256("test", "secret")
            ).create();

            ApiException e = assertThrows(ApiException.class, () -> {
                tokenService.verify(token.getJwtToken());
            });
            assertEquals(e.getMessage(), InvalidToken.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testVerify_UserNotFound() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Jwt jwt = new JwtHS256("test", "secret");
            Token token = MemoryDBTestUtil.createToken(
                    memoryDB,
                    jwt
            );

            TokenService tokenService = new TokenServiceImpl(
                    memoryDB,
                    new MockUserRepository(memoryDB).returnNullOnFindUserById(),
                    jwt
            );

            ApiException e = assertThrows(ApiException.class, () -> {
                tokenService.verify(token.getJwtToken());
            });
            assertEquals(e.getMessage(), UserNotFound.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
