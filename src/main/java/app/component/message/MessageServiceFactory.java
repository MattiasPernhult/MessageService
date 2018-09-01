package app.component.message;

import app.db.MemoryDB;
import app.db.Type;

public class MessageServiceFactory {

    private Type type;
    private MemoryDB memoryDB;

    public MessageServiceFactory(Type type, MemoryDB memoryDB) {
        this.type = type;
        this.memoryDB = memoryDB;
    }

    public MessageService create() {
        switch (this.type) {
            case MEMORY:
                return new MessageServiceImpl(this.memoryDB);
        }

        return null;
    }

}
