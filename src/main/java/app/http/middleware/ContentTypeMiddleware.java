package app.http.middleware;

import app.http.exception.ApiException;
import app.http.transformer.JsonTransformer;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class ContentTypeMiddleware {
    private String value;
    private JsonTransformer jsonTransformer;

    private final String CONTENT_TYPE_HEADER = "Content-Type";

    public ContentTypeMiddleware(String value, JsonTransformer jsonTransformer) {
        this.value = value;
        this.jsonTransformer = jsonTransformer;
    }

    public Filter ensureCorrectRequestContentType = (Request request, Response response) -> {
        String reqContentType = request.headers(CONTENT_TYPE_HEADER);
        if (reqContentType == null || !reqContentType.equals(this.value)) {
            ApiException apiException = ApiException.BadRequest.
                    setDetail(String.format("Invalid %s, must be %s", CONTENT_TYPE_HEADER, this.value));

            halt(apiException.httpStatusCode(), this.jsonTransformer.transformApiException(apiException));
        }
    };

    public Filter middleware = (Request request, Response response) -> {
        response.type(this.value);
    };
}
