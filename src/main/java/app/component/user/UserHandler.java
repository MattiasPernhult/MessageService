package app.component.user;

import app.http.exception.ApiException;
import app.http.parser.JsonParser;
import app.http.transformer.JsonTransformer;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler {

    private UserServiceFactory userServiceFactory;
    private JsonTransformer jsonTransformer;
    private JsonParser jsonParser;

    public UserHandler(
            UserServiceFactory userServiceFactory,
            JsonTransformer jsonTransformer,
            JsonParser jsonParser
    ) {
        this.userServiceFactory = userServiceFactory;
        this.jsonTransformer = jsonTransformer;
        this.jsonParser = jsonParser;
    }

    public Route createUser = (Request request, Response response) -> {
        try {
            User user = this.jsonParser.parseBodyOfCreateUserRequest(request);

            UserService userService = this.userServiceFactory.create();
            userService.create(user);

            response.status(200);
            return this.jsonTransformer.transformUser(user);

        } catch (JsonSyntaxException e) {
            ApiException apiException = ApiException.JsonInvalid.setDetail(e.getMessage());
            response.status(apiException.httpStatusCode());
            return this.jsonTransformer.transformApiException(apiException);

        } catch (ApiException apiException) {
            response.status(apiException.httpStatusCode());
            return this.jsonTransformer.transformApiException(apiException);

        } catch (Exception e) {
            ApiException apiException = ApiException.Unknown.setDetail(e.getMessage());
            response.status(apiException.httpStatusCode());
            return this.jsonTransformer.transformApiException(apiException);
        }
    };
}
