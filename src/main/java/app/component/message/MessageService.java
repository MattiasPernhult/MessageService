package app.component.message;

import app.http.exception.ApiException;

import java.util.List;

public interface MessageService {
    void create(Message message) throws ApiException;

    List<Message> getAll() throws ApiException;

    void update(Message message) throws ApiException;

    void delete(String userId, String messageId) throws ApiException;
}
