package app.component.message;

import app.http.exception.ApiException;

import java.util.List;

public interface MessageRepository {
    List<Message> getAllMessages() throws ApiException;

    void createMessage(Message message) throws ApiException;

    void updateMessage(Message message) throws ApiException;

    void deleteMessage(String userId, String messageId) throws ApiException;

    Message findMessageById(String messageId) throws ApiException;
}
