package apitest;

import app.component.message.Message;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class EndpointTest {

    @Test
    void testAllEndpointsTogether() {
        // User Endpoint
        UserHelper.newUser_missingName();
        UserHelper.newUser_invalidName("ma");
        UserHelper.newUser_invalidName(new String(new char[31]).replace("\0", "a"));

        // User Endpoint
        String nameForUser1 = UUID.randomUUID().toString().substring(10);
        String nameForUser2 = UUID.randomUUID().toString().substring(10);
        String idForUser1 = UserHelper.newUser(nameForUser1);
        String idForUser2 = UserHelper.newUser(nameForUser2);
        UserHelper.newUser_nameAlreadyExists(nameForUser1);

        // Token Endpoint
        String tokenForUser1 = TokenHelper.createToken(idForUser1);
        String tokenForUser2 = TokenHelper.createToken(idForUser2);
        TokenHelper.createToken_userNotFound(String.format("%s1", idForUser1));

        int numberOfMessages = MessageHelper.allMessages_getSize();
        MessageHelper.allMessages_checkSize(numberOfMessages);

        // Create Message Endpoint
        MessageHelper.createMessage_textMissing(idForUser1, tokenForUser1);
        MessageHelper.createMessage_textInvalid(idForUser1, tokenForUser1, "ab");
        MessageHelper.createMessage_createdAtMissing(idForUser2, tokenForUser2);
        MessageHelper.createMessage_createdAtTimestampMissing(idForUser2, tokenForUser2);
        MessageHelper.createMessage_createdAtTimestampInvalid(idForUser2, tokenForUser2);
        MessageHelper.createMessage_createdAtOffsetMissing(idForUser1, tokenForUser1);
        MessageHelper.createMessage_createdAtOffsetInvalid(idForUser1, tokenForUser1, 50401);
        MessageHelper.createMessage_createdAtOffsetInvalid(idForUser1, tokenForUser1, -43201);
        MessageHelper.createMessage_invalidToken(String.format("%s1", idForUser2), tokenForUser2);

        String messageId1ForUser1 = MessageHelper.createMessage(idForUser1, tokenForUser1);
        String messageId2ForUser1 = MessageHelper.createMessage(idForUser1, tokenForUser1);
        String messageId1ForUser2 = MessageHelper.createMessage(idForUser2, tokenForUser2);
        String messageId2ForUser2 = MessageHelper.createMessage(idForUser2, tokenForUser2);

        numberOfMessages += 4;
        MessageHelper.allMessages_checkSize(numberOfMessages);

        // Update Message Endpoint
        MessageHelper.updateMessage_messageNotFound(
                idForUser1,
                String.format("%s1", messageId1ForUser1),
                tokenForUser1
        );
        MessageHelper.updateMessage_notAllowedToUpdateMessage(
                idForUser1,
                messageId1ForUser2,
                tokenForUser1
        );
        MessageHelper.updateMessage_notAllowedToUpdateMessage(
                idForUser2,
                messageId1ForUser1,
                tokenForUser2
        );
        MessageHelper.updateMessage(
                "user 1: new text for message 2",
                idForUser1,
                messageId2ForUser1,
                tokenForUser1
        );
        MessageHelper.updateMessage(
                "user 2: new text for message 2",
                idForUser2,
                messageId2ForUser2,
                tokenForUser2
        );
        MessageHelper.allMessages_checkSize(numberOfMessages);


        // Delete Message Endpoint
        MessageHelper.deleteMessage_messageNotFound(
                idForUser1,
                String.format("%s1", messageId1ForUser1),
                tokenForUser1
        );
        MessageHelper.deleteMessage_notAllowedToUpdateMessage(
                idForUser1,
                messageId1ForUser2,
                tokenForUser1
        );
        MessageHelper.deleteMessage_notAllowedToUpdateMessage(
                idForUser2,
                messageId1ForUser1,
                tokenForUser2
        );
        MessageHelper.deleteMessage(
                idForUser1,
                messageId2ForUser1,
                tokenForUser1
        );
        MessageHelper.deleteMessage(
                idForUser2,
                messageId2ForUser2,
                tokenForUser2
        );

        numberOfMessages -= 2;
        MessageHelper.allMessages_checkSize(numberOfMessages);
    }
}
