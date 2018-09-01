package app.component.message;

import app.http.exception.ApiException;

import java.util.List;

public class MockMessageRepository implements MessageRepository {

    private MessageRepository realRepo;
    private boolean failOnCreate;
    private boolean failOnFetchAll;
    private boolean failOnUpdate;
    private boolean failOnDelete;

    MockMessageRepository(MessageRepository realRepo) {
        this.realRepo = realRepo;
    }

    MockMessageRepository failOnCreate() {
        this.failOnCreate = true;
        return this;
    }

    MockMessageRepository failOnFetchAll() {
        this.failOnFetchAll = true;
        return this;
    }

    MockMessageRepository failOnUpdate() {
        this.failOnUpdate = true;
        return this;
    }

    MockMessageRepository failOnDelete() {
        this.failOnDelete = true;
        return this;
    }

    @Override
    public List<Message> getAllMessages() throws ApiException {
        if (this.failOnFetchAll) {
            throw ApiException.ServerError;
        }

        return this.realRepo.getAllMessages();
    }

    @Override
    public void createMessage(Message message) throws ApiException {
        if (failOnCreate) {
            throw ApiException.ServerError;
        }

        this.realRepo.createMessage(message);
    }

    @Override
    public void updateMessage(Message message) throws ApiException {
        if (failOnUpdate) {
            throw ApiException.ServerError;
        }

        this.realRepo.updateMessage(message);
    }

    @Override
    public void deleteMessage(String userId, String messageId) throws ApiException {
        if (failOnDelete) {
            throw ApiException.ServerError;
        }

        this.realRepo.deleteMessage(userId, messageId);
    }

    @Override
    public Message findMessageById(String messageId) throws ApiException {
        return this.realRepo.findMessageById(messageId);
    }
}
