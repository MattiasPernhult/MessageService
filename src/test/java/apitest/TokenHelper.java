package apitest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

class TokenHelper {

    static String createToken(String userId) {
        ExtractableResponse<Response> response = performCreateTokenRequest(userId).
                then().
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON).
                and().
                body("token", Matchers.notNullValue())
                .extract();

        return response.body().jsonPath().get("token");
    }

    static void createToken_userNotFound(String userId) {
        Helper.verifyError(
                performCreateTokenRequest(userId),
                404,
                4002,
                "User not found",
                null
        );
    }

    private static Response performCreateTokenRequest(String userId) {
        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> body = new HashMap<>();

        return given().
                pathParam("user_id", userId).
                body(gson.toJson(body)).
                header("Content-Type", "application/json").
                when().
                post("/api/v1/users/{user_id}/tokens");
    }

}
