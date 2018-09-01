package app.http.transformer;

import app.component.token.Token;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class TokenResponse {
    @Expose(deserialize = false)
    @SerializedName("token")
    private String token;

    private TokenResponse(String token) {
        this.token = token;
    }

    static TokenResponse fromToken(Token token) {
        return new TokenResponse(token.getJwtToken());
    }
}
