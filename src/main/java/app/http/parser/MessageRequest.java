package app.http.parser;

import app.Constant;
import app.component.message.Message;
import app.component.user.User;
import app.http.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import spark.Request;

import java.util.UUID;

class MessageRequest {

    private String id;

    @Expose(serialize = false)
    @SerializedName("text")
    private String text;

    @Expose(serialize = false)
    @SerializedName("created_at")
    private ApiDateTime createdAt;

    private User user;

    static Message fromRequest(Gson gson, Request request, boolean validateId) throws ApiException, JsonSyntaxException {
        MessageRequest messageRequest = gson.fromJson(request.body(), MessageRequest.class);

        messageRequest.id = request.params(Constant.MESSAGE_ID_PARAM);
        messageRequest.user = request.session().attribute(Constant.USER_REQ_ATTRIBUTE);

        messageRequest.validate(validateId);

        return messageRequest.convert();
    }

    private void validate(boolean validateId) throws ApiException {
        if (validateId) {
            if (this.id == null || this.text.isEmpty()) {
                throw ApiException.ParameterMissing.setDetail("id");
            }
        }

        if (this.text == null || this.text.isEmpty()) {
            throw ApiException.ParameterMissing.setDetail("text");
        }

        if (this.text.length() < 3) {
            throw ApiException.ParameterInvalid.setDetail("text must have a min length of 3 chars");
        }

        if (this.createdAt == null) {
            throw ApiException.ParameterMissing.setDetail("created_at");
        }
        this.createdAt.validate("created_at");
    }

    private Message convert() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }

        return new Message(
                this.id,
                this.text,
                this.createdAt.convert(),
                this.user
        );
    }
}
