package apitest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

class UserHelper {

    static String newUser(String name) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);

        ExtractableResponse<Response> response = performCreateUserRequest(body).
                then().
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON).
                and().
                body("name", Matchers.equalTo(name)).
                body("id", Matchers.notNullValue())
                .extract();

        return response.body().jsonPath().get("id");
    }

    static void newUser_missingName() {
        HashMap<String, Object> body = new HashMap<>();

        Helper.verifyError(
                performCreateUserRequest(body),
                400,
                3001,
                "Parameter Missing",
                "name"
        );
    }

    static void newUser_invalidName(String name) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);

        Helper.verifyError(
                performCreateUserRequest(body),
                400,
                3002,
                "Parameter Invalid",
                null
        );
    }

    static void newUser_nameAlreadyExists(String name) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);

        Helper.verifyError(
                performCreateUserRequest(body),
                400,
                3004,
                "User already exists",
                null
        );
    }

    private static Response performCreateUserRequest(HashMap<String, Object> body) {
        Gson gson = new GsonBuilder().create();

        return given().
                body(gson.toJson(body)).
                header("Content-Type", "application/json").
                when().
                post("/api/v1/users");
    }
}
