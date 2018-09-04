package app.component.user;

import app.db.MemoryDB;

public class UserServiceMemoryDBFactory implements UserServiceFactory {

    private MemoryDB memoryDB;

    public UserServiceMemoryDBFactory(MemoryDB memoryDB) {
        this.memoryDB = memoryDB;
    }

    public UserService create() {
        return new UserServiceImpl(this.memoryDB);
    }
}
