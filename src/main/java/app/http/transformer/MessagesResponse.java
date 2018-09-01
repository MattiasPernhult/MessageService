package app.http.transformer;

import app.component.message.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class MessagesResponse {
    @Expose(deserialize = false)
    @SerializedName("messages")
    private List<Object> messages;

    private MessagesResponse(List<Message> msgs) {
        this.messages = new ArrayList<>();
        for (Message message : msgs) {
            this.messages.add(MessageResponse.fromMessage(message));
        }
    }

    static MessagesResponse fromMessages(List<Message> messages) {
        return new MessagesResponse(messages);
    }
}
