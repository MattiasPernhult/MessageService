package app.component.message;

import app.http.exception.ApiException;

import java.util.List;

class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;

    MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void create(Message message) throws ApiException {
        this.messageRepository.createMessage(message);
    }

    @Override
    public List<Message> getAll() throws ApiException {
        return this.messageRepository.getAllMessages();
    }

    @Override
    public void update(Message message) throws ApiException {
        this.checkIfUserOwnsMessage(message.getUser().getId(), message.getId());
        this.messageRepository.updateMessage(message);
    }

    @Override
    public void delete(String userId, String messageId) throws ApiException {
        this.checkIfUserOwnsMessage(userId, messageId);
        this.messageRepository.deleteMessage(userId, messageId);
    }

    private void checkIfUserOwnsMessage(String userId, String messageId) throws ApiException {
        Message storedMessage = this.messageRepository.findMessageById(messageId);
        if (storedMessage == null) {
            throw ApiException.MessageNotFound;
        }

        if (!storedMessage.getUser().getId().equals(userId)) {
            throw ApiException.UserNotAllowedToModifyMessage;
        }
    }
}
