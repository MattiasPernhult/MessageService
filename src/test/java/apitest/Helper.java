package apitest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;

class Helper {

    static void verifyError(Response response, int statusCode, int errorCode, String errorMessage, String detail) {
        ValidatableResponse validatableResponse = response.
                then().
                assertThat().
                statusCode(statusCode).
                contentType(ContentType.JSON).
                and().
                body("error.code", Matchers.equalTo(errorCode)).
                body("error.reason", Matchers.equalTo(errorMessage));

        if (detail != null) {
            validatableResponse.body("error.detail", Matchers.equalTo(detail));
        }
    }

}
