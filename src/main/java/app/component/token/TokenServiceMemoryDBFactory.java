package app.component.token;

import app.db.MemoryDB;
import app.service.jwt.Jwt;

public class TokenServiceMemoryDBFactory implements TokenServiceFactory {

    private MemoryDB memoryDB;
    private Jwt jwt;

    public TokenServiceMemoryDBFactory(MemoryDB memoryDB, Jwt jwt) {
        this.memoryDB = memoryDB;
        this.jwt = jwt;
    }

    public TokenService create() {
        return new TokenServiceImpl(this.memoryDB, this.memoryDB, jwt);
    }
}
