package apitest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

class MessageHelper {

    static int allMessages_getSize() {
        List<Object> messages = performGetAllMessagesRequest().
                extract().
                body().
                jsonPath().
                get("messages");
        return messages.size();
    }

    static void allMessages_checkSize(int size) {
        performGetAllMessagesRequest().
                body("messages", Matchers.hasSize(size));
    }

    private static ValidatableResponse performGetAllMessagesRequest() {
        return given().
                when().
                get("/api/v1/messages").
                then().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON);
    }

    static String createMessage(String userId, String token) {
        HashMap<String, Object> createdAt = new HashMap<>();
        createdAt.put("timestamp", 1535798728);
        createdAt.put("offset", 7200);

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");
        body.put("created_at", createdAt);

        return performCreateMessageRequest(body, userId, token).
                then().
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON).
                body("text", Matchers.equalTo("Example text")).
                body("created_at.timestamp", Matchers.equalTo(1535798728)).
                body("created_at.offset", Matchers.equalTo(7200)).
                body("user.id", Matchers.equalTo(userId)).
                body("id", Matchers.notNullValue()).
                extract().
                body().
                jsonPath().
                get("id");
    }

    static void createMessage_textMissing(String userId, String token) {
        HashMap<String, Object> body = new HashMap<>();

        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3001,
                "Parameter Missing",
                "text"
        );
    }

    static void createMessage_textInvalid(String userId, String token, String text) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("text", text);

        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3002,
                "Parameter Invalid",
                null
        );
    }

    static void createMessage_createdAtMissing(String userId, String token) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");

        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3001,
                "Parameter Missing",
                "created_at"
        );
    }

    static void createMessage_createdAtTimestampMissing(String userId, String token) {
        HashMap<String, Object> createdAt = new HashMap<>();

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");
        body.put("created_at", createdAt);


        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3001,
                "Parameter Missing",
                "created_at.timestamp"
        );
    }

    static void createMessage_createdAtTimestampInvalid(String userId, String token) {
        HashMap<String, Object> createdAt = new HashMap<>();
        createdAt.put("timestamp", 0);

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");
        body.put("created_at", createdAt);


        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3002,
                "Parameter Invalid",
                "created_at.timestamp"
        );
    }

    static void createMessage_createdAtOffsetMissing(String userId, String token) {
        HashMap<String, Object> createdAt = new HashMap<>();
        createdAt.put("timestamp", 1535798728);

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");
        body.put("created_at", createdAt);


        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3001,
                "Parameter Missing",
                "created_at.offset"
        );
    }

    static void createMessage_createdAtOffsetInvalid(String userId, String token, int offset) {
        HashMap<String, Object> createdAt = new HashMap<>();
        createdAt.put("timestamp", 1535798728);
        createdAt.put("offset", offset);

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");
        body.put("created_at", createdAt);


        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                400,
                3002,
                "Parameter Invalid",
                "created_at.offset"
        );
    }

    static void createMessage_invalidToken(String userId, String token) {
        HashMap<String, Object> body = validBody();

        Helper.verifyError(
                performCreateMessageRequest(body, userId, token),
                401,
                2001,
                "Invalid Token",
                null
        );
    }

    private static Response performCreateMessageRequest(HashMap<String, Object> body, String userId, String token) {
        Gson gson = new GsonBuilder().create();

        return given().
                pathParam("user_id", userId).
                body(gson.toJson(body)).
                header("Content-Type", "application/json").
                header("Authorization", String.format("Bearer %s", token)).
                when().
                post("/api/v1/users/{user_id}/messages");
    }

    static void updateMessage(String text, String userId, String messageId, String token) {
        HashMap<String, Object> body = validBody();
        body.put("text", text);

        performUpdateMessageRequest(body, userId, messageId, token).
                then().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON).
                body("text", Matchers.equalTo(text)).
                body("created_at.timestamp", Matchers.equalTo(1535798728)).
                body("created_at.offset", Matchers.equalTo(7200)).
                body("user.id", Matchers.equalTo(userId)).
                body("id", Matchers.notNullValue());
    }

    static void updateMessage_messageNotFound(String userId, String messageId, String token) {
        HashMap<String, Object> body = validBody();

        Helper.verifyError(
                performUpdateMessageRequest(body, userId, messageId, token),
                404,
                4003,
                "Message not found",
                null
        );
    }

    static void updateMessage_notAllowedToUpdateMessage(String userId, String messageId, String token) {
        HashMap<String, Object> body = validBody();
        Helper.verifyError(
                performUpdateMessageRequest(body, userId, messageId, token),
                400,
                3005,
                "User not allowed to update or delete message of another user",
                null
        );
    }

    private static Response performUpdateMessageRequest(
            HashMap<String, Object> body,
            String userId,
            String messageId,
            String token
    ) {
        Gson gson = new GsonBuilder().create();

        return given().
                pathParam("user_id", userId).
                pathParam("message_id", messageId).
                body(gson.toJson(body)).
                header("Content-Type", "application/json").
                header("Authorization", String.format("Bearer %s", token)).
                when().
                put("/api/v1/users/{user_id}/messages/{message_id}");
    }

    static void deleteMessage(String userId, String messageId, String token) {
        performDeleteMessageRequest(userId, messageId, token).
                then().
                assertThat().
                statusCode(204).
                contentType(ContentType.JSON);
    }

    static void deleteMessage_messageNotFound(String userId, String messageId, String token) {
        HashMap<String, Object> body = validBody();

        Helper.verifyError(
                performDeleteMessageRequest(userId, messageId, token),
                404,
                4003,
                "Message not found",
                null
        );
    }

    static void deleteMessage_notAllowedToUpdateMessage(String userId, String messageId, String token) {
        HashMap<String, Object> body = validBody();
        Helper.verifyError(
                performDeleteMessageRequest(userId, messageId, token),
                400,
                3005,
                "User not allowed to update or delete message of another user",
                null
        );
    }

    private static Response performDeleteMessageRequest(
            String userId,
            String messageId,
            String token
    ) {
        Gson gson = new GsonBuilder().create();

        return given().
                pathParam("user_id", userId).
                pathParam("message_id", messageId).
                header("Content-Type", "application/json").
                header("Authorization", String.format("Bearer %s", token)).
                when().
                delete("/api/v1/users/{user_id}/messages/{message_id}");
    }

    private static HashMap<String, Object> validBody() {
        HashMap<String, Object> createdAt = new HashMap<>();
        createdAt.put("timestamp", 1535798728);
        createdAt.put("offset", 7200);

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "Example text");
        body.put("created_at", createdAt);

        return body;
    }
}
