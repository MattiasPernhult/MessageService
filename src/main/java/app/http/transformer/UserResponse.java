package app.http.transformer;

import app.component.user.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class UserResponse {
    @Expose(deserialize = false)
    @SerializedName("id")
    private String id;

    @Expose(deserialize = false)
    @SerializedName("name")
    private String name;

    private UserResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
