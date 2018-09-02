package app.db;

import app.component.message.Message;
import app.component.message.MessageRepository;
import app.component.token.Token;
import app.component.token.TokenRepository;
import app.component.user.User;
import app.component.user.UserRepository;
import app.http.exception.ApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryDB implements
        UserRepository,
        TokenRepository,
        MessageRepository {

    private ConcurrentHashMap<String, User> users;
    private ConcurrentHashMap<String, Token> tokens;
    private ConcurrentHashMap<String, Message> messages;

    public MemoryDB() {
        this.users = new ConcurrentHashMap<>();
        this.tokens = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
    }

    //
    // MessageRepository methods
    //
    @Override
    public List<Message> getAllMessages() throws ApiException {
        List<Message> messages = new ArrayList<>(this.messages.values());
        messages.sort(new Message().getSorter());
        return messages;
    }

    @Override
    public void createMessage(Message message) throws ApiException {
        this.messages.put(message.getId(), message);
    }

    @Override
    public void updateMessage(Message message) throws ApiException {
        this.messages.put(message.getId(), message);
    }

    @Override
    public void deleteMessage(String userId, String messageId) throws ApiException {
        this.messages.remove(messageId);
    }

    @Override
    public Message findMessageById(String messageId) throws ApiException {
        return this.messages.get(messageId);
    }

    //
    // UserRepository methods
    //
    @Override
    public void createUser(User user) throws ApiException {
        this.users.put(user.getId(), user);
    }

    @Override
    public User findUserByName(String name) throws ApiException {
        Collection<User> user = this.users.values();
        for (User u : user) {
            if (u.getName().equals(name)) {
                return u;
            }
        }

        return null;
    }

    @Override
    public User findUserById(String id) throws ApiException {
        return this.users.get(id);
    }

    //
    // TokenRepository methods
    //
    @Override
    public void createToken(Token token) throws ApiException {
        this.tokens.put(token.getJwtToken(), token);
    }

    @Override
    public Token findTokenByJwtToken(String jwtToken) throws ApiException {
        return this.tokens.get(jwtToken);
    }
}
