package app.http.parser;

import app.component.user.User;
import app.http.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import spark.Request;

import java.util.UUID;

class UserRequest {

    @Expose(serialize = false)
    @SerializedName("name")
    private String name;

    static User fromRequest(Gson gson, Request request) throws ApiException, JsonSyntaxException {
        UserRequest userRequest = gson.fromJson(request.body(), UserRequest.class);
        userRequest.validate();

        return userRequest.convert();
    }

    private void validate() throws ApiException {
        if (this.name == null || this.name.isEmpty()) {
            throw ApiException.ParameterMissing.setDetail("name");
        }

        if (this.name.length() < 3 || this.name.length() > 30) {
            throw ApiException.ParameterInvalid.setDetail(
                    "name must have a min length of 3 chars and max length of 30 chars"
            );
        }
    }

    private User convert() {
        return new User(UUID.randomUUID().toString(), this.name);
    }
}
