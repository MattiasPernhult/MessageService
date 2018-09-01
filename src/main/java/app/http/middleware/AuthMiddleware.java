package app.http.middleware;

import app.Constant;
import app.component.token.Token;
import app.component.token.TokenService;
import app.component.token.TokenServiceFactory;
import app.http.exception.ApiException;
import app.http.transformer.JsonTransformer;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class AuthMiddleware {

    private TokenServiceFactory tokenServiceFactory;
    private JsonTransformer jsonTransformer;

    private final String AUTH_HEADER = "Authorization";
    private final String AUTH_TOKEN_PREFIX = "Bearer";

    public AuthMiddleware(
            TokenServiceFactory tokenServiceFactory,
            JsonTransformer jsonTransformer
    ) {
        this.tokenServiceFactory = tokenServiceFactory;
        this.jsonTransformer = jsonTransformer;
    }

    public Filter authenticateUser = (Request request, Response response) -> {

        try {
            String header = request.headers(AUTH_HEADER);
            String[] parts = header.split(" ");
            if (parts.length != 2 || !parts[0].equals(AUTH_TOKEN_PREFIX)) {
                throw ApiException.AuthorizationTokenMalformed;
            }

            String jwtToken = parts[1];
            String userId = request.params("user_id");
            if (userId == null) {
                throw ApiException.ParameterMissing.setDetail("user_id");
            }

            TokenService tokenService = this.tokenServiceFactory.create();
            Token token = tokenService.verify(jwtToken, userId);

            request.session().attribute(Constant.USER_REQ_ATTRIBUTE, token.getUser());
        } catch (ApiException apiException) {
            halt(
                    apiException.httpStatusCode(),
                    this.jsonTransformer.transformApiException(apiException)
            );

        } catch (Exception e) {
            ApiException apiException = ApiException.Unknown.setDetail(e.getMessage());
            halt(
                    apiException.httpStatusCode(),
                    this.jsonTransformer.transformApiException(apiException)
            );
        }
    };
}
