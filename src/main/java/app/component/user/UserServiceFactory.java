package app.component.user;

import app.db.MemoryDB;
import app.db.Type;

public class UserServiceFactory {

    private Type type;
    private MemoryDB memoryDB;

    public UserServiceFactory(Type type, MemoryDB memoryDB) {
        this.type = type;
        this.memoryDB = memoryDB;
    }

    public UserService create() {
        switch (type) {
            case MEMORY:
                return new UserServiceImpl(this.memoryDB);
        }

        return null;
    }
}
