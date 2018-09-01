package app.http.transformer;

import app.component.message.Message;
import app.component.token.Token;
import app.component.user.User;
import app.http.exception.ApiException;
import com.google.gson.Gson;

import java.util.List;

public class JsonTransformer {

    private Gson gson;

    public JsonTransformer(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }

    public String transformUser(User user) {
        return this.gson.toJson(UserResponse.fromUser(user));
    }

    public String transformToken(Token token) {
        return this.gson.toJson(TokenResponse.fromToken(token));
    }

    public String transformMessage(Message message) {
        return this.gson.toJson(MessageResponse.fromMessage(message));
    }

    public String transformMessages(List<Message> messages) {
        return this.gson.toJson(MessagesResponse.fromMessages(messages));
    }

    public String transformObject(Object o) {
        return this.gson.toJson(o);
    }

    public String transformApiException(ApiException apiException) {
        return this.gson.toJson(ApiError.fromApiException(apiException));
    }
}
