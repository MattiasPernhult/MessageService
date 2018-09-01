package apitest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;
import java.util.zip.ZipEntry;

import static io.restassured.RestAssured.given;

class UserCreateEndpointTest {

    @Test
    void test_UserCreate_ShouldBeOK() {
        newUser_MissingName();
        newUser_InvalidName("ma");
        newUser_InvalidName(new String(new char[31]).replace("\0", "a"));

        String nameForUser1 = UUID.randomUUID().toString().substring(10);
        String nameForUser2 = UUID.randomUUID().toString().substring(10);
        String idForUser1 = newUser(nameForUser1);
        String idForUser2 = newUser(nameForUser1);

        //  ok
        //  user not found
        //  text missing
        //  text invalid
        //  created_at missing
        //  created_at.timestamp missing
        //  created_at.timestamp invalid
        //  created_at.offset missing
        //  created_at.offset invalid
    }

    private void newUser_MissingName() {
        HashMap<String, Object> body = new HashMap<>();
        performCreateUserRequest(body).
                then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                and().
                body("error.code", Matchers.equalTo(3001)).
                body("error.reason", Matchers.equalTo("Parameter Missing")).
                body("error.detail", Matchers.equalTo("name"));
    }

    private void newUser_InvalidName(String name) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);
        performCreateUserRequest(body).
                then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                and().
                body("error.code", Matchers.equalTo(3002)).
                body("error.reason", Matchers.equalTo("Parameter Invalid")).
                body("error.detail", Matchers.notNullValue());
    }

    private void newUser_NameAlreadyExists(String name) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);

        performCreateUserRequest(body).
                then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                and().
                body("error.code", Matchers.equalTo(3004)).
                body("error.reason", Matchers.equalTo("User already exists"));
    }

    private String newUser(String name) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);

        ExtractableResponse<Response> l = performCreateUserRequest(body).
                then().
                assertThat().
                statusCode(200).contentType(ContentType.JSON).
                and().
                body("name", Matchers.equalTo(name)).
                body("id", Matchers.notNullValue())
                .extract();

        return l.body().jsonPath().get("id");
    }

    private Response performCreateUserRequest(HashMap<String, Object> body) {
        Gson gson = new GsonBuilder().create();

        return given().body(gson.toJson(body)).header("Content-Type", "application/json").
                when().post("http://localhost:8080/api/v1/users");
    }

}
