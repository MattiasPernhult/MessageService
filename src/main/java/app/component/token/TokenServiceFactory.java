package app.component.token;

import app.db.MemoryDB;
import app.db.Type;
import app.service.jwt.Jwt;

public class TokenServiceFactory {

    private Type type;
    private MemoryDB memoryDB;
    private Jwt jwt;

    public TokenServiceFactory(Type type, MemoryDB memoryDB, Jwt jwt) {
        this.type = type;
        this.memoryDB = memoryDB;
        this.jwt = jwt;
    }

    public TokenService create() {
        switch (this.type) {
            case MEMORY:
                return new TokenServiceImpl(this.memoryDB, this.memoryDB, jwt);
        }

        return null;
    }
}
