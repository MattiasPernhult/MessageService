package app.http.parser;

import app.Constant;
import app.component.token.Token;
import app.http.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import spark.Request;

class TokenRequest {

    private String userId;

    static Token fromRequest(Gson gson, Request request) throws ApiException, JsonSyntaxException {
        TokenRequest tokenRequest = gson.fromJson(request.body(), TokenRequest.class);
        tokenRequest.userId = request.params(Constant.USER_ID_PARAM);
        tokenRequest.validate();

        return tokenRequest.convert();
    }

    private void validate() throws ApiException {
        if (this.userId == null || this.userId.isEmpty()) {
            throw ApiException.ParameterMissing.setDetail(Constant.USER_ID_PARAM);
        }
    }

    private Token convert() {
        return new Token(this.userId);
    }
}
