package app.component.message;

import app.db.MemoryDB;

public class MessageServiceMemoryDBFactory implements MessageServiceFactory {

    private MemoryDB memoryDB;

    public MessageServiceMemoryDBFactory(MemoryDB memoryDB) {
        this.memoryDB = memoryDB;
    }

    public MessageService create() {
        return new MessageServiceImpl(this.memoryDB);
    }

}
