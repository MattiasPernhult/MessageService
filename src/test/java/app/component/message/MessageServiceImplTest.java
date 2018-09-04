package app.component.message;

import app.component.user.User;
import app.db.MemoryDB;
import app.db.MemoryDBTestUtil;
import app.http.exception.ApiException;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest {

    @Test
    void testCreate_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            User user = MemoryDBTestUtil.createUser(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            Message message = new Message(
                    "1",
                    "Test text",
                    new DateTime(),
                    user
            );
            messageService.create(message);

            List<Message> messages = messageService.getAll();
            assertEquals(1, messages.size());
            assertEquals(message, messages.get(0));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreate_Fails() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            User user = MemoryDBTestUtil.createUser(memoryDB);

            MessageService messageService = new MessageServiceImpl(
                    new MockMessageRepository(memoryDB).failOnCreate()
            );

            Message message = new Message(
                    "1",
                    "Test text",
                    new DateTime(),
                    user
            );

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.create(message);
            });
            assertEquals(ApiException.ServerError.getMessage(), e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetAll_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message1 = MemoryDBTestUtil.createMessage(memoryDB);
            Message message2 = MemoryDBTestUtil.createMessage(memoryDB);
            Message message3 = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(3, messages.size());
            assertEquals(message1, messages.get(0));
            assertEquals(message2, messages.get(1));
            assertEquals(message3, messages.get(2));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetAll_Fails() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            MemoryDBTestUtil.createMessage(memoryDB);
            MemoryDBTestUtil.createMessage(memoryDB);
            MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceImpl(
                    new MockMessageRepository(memoryDB).failOnFetchAll()
            );

            ApiException e = assertThrows(ApiException.class, messageService::getAll);
            assertEquals(ApiException.ServerError.getMessage(), e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testUpdate_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(1, messages.size());
            assertEquals(message, messages.get(0));

            Message newMessage = new Message(
                    message.getId(),
                    "Updated text",
                    message.getCreatedAt(),
                    message.getUser()
            );

            messageService.update(newMessage);

            messages = messageService.getAll();
            assertEquals(1, messages.size());
            assertEquals(newMessage, messages.get(0));
            assertNotEquals(message, messages.get(0));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testUpdate_MessageNotFound() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(1, messages.size());
            assertEquals(message, messages.get(0));

            Message newMessage = new Message(
                    UUID.randomUUID().toString(),
                    "Updated text",
                    message.getCreatedAt(),
                    message.getUser()
            );

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.update(newMessage);
            });
            assertEquals(ApiException.MessageNotFound.getMessage(), e.getMessage());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testUpdate_NotAllowedToUpdate() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message1 = MemoryDBTestUtil.createMessage(memoryDB);
            Message message2 = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(2, messages.size());

            Message newMessage = new Message(
                    message1.getId(),
                    "Updated text",
                    message1.getCreatedAt(),
                    message2.getUser()
            );

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.update(newMessage);
            });
            assertEquals(ApiException.UserNotAllowedToModifyMessage.getMessage(), e.getMessage());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testUpdate_FailWhenUpdating() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceImpl(
                    new MockMessageRepository(memoryDB).failOnUpdate()
            );

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.update(message);
            });
            assertEquals(ApiException.ServerError.getMessage(), e.getMessage());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDelete_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(1, messages.size());
            assertEquals(message, messages.get(0));

            messageService.delete(message.getUser().getId(), message.getId());

            messages = messageService.getAll();
            assertEquals(0, messages.size());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDelete_MessageNotFound() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(1, messages.size());
            assertEquals(message, messages.get(0));

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.delete(message.getUser().getId(), UUID.randomUUID().toString());
            });
            assertEquals(ApiException.MessageNotFound.getMessage(), e.getMessage());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDelete_NotAllowedToUpdate() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message1 = MemoryDBTestUtil.createMessage(memoryDB);
            Message message2 = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceMemoryDBFactory(
                    memoryDB
            ).create();

            List<Message> messages = messageService.getAll();
            assertEquals(2, messages.size());

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.delete(message1.getUser().getId(), message2.getId());
            });
            assertEquals(ApiException.UserNotAllowedToModifyMessage.getMessage(), e.getMessage());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDelete_FailWhenDeleting() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            Message message = MemoryDBTestUtil.createMessage(memoryDB);

            MessageService messageService = new MessageServiceImpl(
                    new MockMessageRepository(memoryDB).failOnDelete()
            );

            ApiException e = assertThrows(ApiException.class, () -> {
                messageService.delete(message.getUser().getId(), message.getId());
            });
            assertEquals(ApiException.ServerError.getMessage(), e.getMessage());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
