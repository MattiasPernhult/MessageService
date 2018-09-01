package app.db;

import app.component.message.Message;
import app.component.token.Token;
import app.component.user.User;
import app.http.exception.ApiException;
import app.service.jwt.ClaimData;
import app.service.jwt.Jwt;
import org.joda.time.DateTime;

import java.util.UUID;

public class MemoryDBTestUtil {

    public static User createUser(MemoryDB db) throws ApiException {
        User user = new User(
                UUID.randomUUID().toString(),
                String.format("Kalle Karlsson %s", UUID.randomUUID().toString())
        );
        db.createUser(user);
        return user;
    }

    public static Token createToken(MemoryDB db, Jwt jwt) throws ApiException {
        User user = createUser(db);
        Token token = new Token();
        token.setUser(user);
        token.setJwtToken(jwt.generateToken(new ClaimData(user.getId())));

        db.createToken(token);
        return token;
    }

    public static Message createMessage(MemoryDB db) throws ApiException, InterruptedException {
        User user = createUser(db);
        Message message = new Message(
                UUID.randomUUID().toString(),
                "Test text",
                new DateTime(),
                user
        );

        Thread.sleep(200);

        db.createMessage(message);
        return message;
    }

}
