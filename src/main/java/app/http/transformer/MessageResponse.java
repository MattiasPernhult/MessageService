package app.http.transformer;

import app.component.message.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class MessageResponse {

    @Expose(deserialize = false)
    @SerializedName("id")
    private String id;

    @Expose(deserialize = false)
    @SerializedName("text")
    private String text;

    @Expose(deserialize = false)
    @SerializedName("created_at")
    private ApiDateTimeResponse createdAt;

    @Expose(deserialize = false)
    @SerializedName("user")
    private UserResponse user;

    private MessageResponse(String id, String text, ApiDateTimeResponse createdAt, UserResponse user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }

    static MessageResponse fromMessage(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getText(),
                ApiDateTimeResponse.fromDateTime(message.getCreatedAt()),
                UserResponse.fromUser(message.getUser())
        );
    }
}
