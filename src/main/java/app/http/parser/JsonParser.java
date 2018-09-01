package app.http.parser;

import app.component.message.Message;
import app.component.token.Token;
import app.component.user.User;
import app.http.exception.ApiException;
import com.google.gson.Gson;
import spark.Request;

public class JsonParser {

    private Gson gson;

    public JsonParser(Gson gson) {
        this.gson = gson;
    }

    public User parseBodyOfCreateUserRequest(Request request) throws ApiException {
        return UserRequest.fromRequest(this.gson, request);
    }

    public Token parseBodyOfCreateTokenRequest(Request request) throws ApiException {
        return TokenRequest.fromRequest(this.gson, request);
    }

    public Message parseBodyOfCreateMessageRequest(Request request) throws ApiException {
        return MessageRequest.fromRequest(this.gson, request, false);
    }

    public Message parseBodyOfUpdateMessageRequest(Request request) throws ApiException {
        return MessageRequest.fromRequest(this.gson, request, true);
    }
}
