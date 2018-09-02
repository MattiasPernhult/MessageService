package app.component.token;

import app.http.exception.ApiException;
import app.http.parser.JsonParser;
import app.http.transformer.JsonTransformer;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;
import spark.Route;

public class TokenHandler {

    private TokenServiceFactory tokenServiceFactory;
    private JsonTransformer jsonTransformer;
    private JsonParser jsonParser;

    public TokenHandler(
            TokenServiceFactory tokenServiceFactory,
            JsonTransformer jsonTransformer,
            JsonParser jsonParser
    ) {
        this.tokenServiceFactory = tokenServiceFactory;
        this.jsonTransformer = jsonTransformer;
        this.jsonParser = jsonParser;
    }

    public Route createToken = (Request request, Response response) -> {
        try {
            Token token = this.jsonParser.parseBodyOfCreateTokenRequest(request);

            TokenService tokenService = this.tokenServiceFactory.create();
            tokenService.create(token);

            response.status(201);
            return this.jsonTransformer.transformToken(token);

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
