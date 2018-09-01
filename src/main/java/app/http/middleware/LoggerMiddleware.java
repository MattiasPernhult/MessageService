package app.http.middleware;

import app.http.transformer.JsonTransformer;
import app.service.logger.Logger;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import spark.Filter;
import spark.Request;
import spark.Response;

public class LoggerMiddleware {

    private Logger logger;
    private JsonTransformer jsonTransformer;

    public LoggerMiddleware(Logger logger, JsonTransformer jsonTransformer) {
        this.logger = logger;
        this.jsonTransformer = jsonTransformer;
    }

    public Filter middleware = (Request request, Response response) -> {

        LogData logData = new LogData(
                request.requestMethod(),
                request.uri(),
                request.body(),
                response.body(),
                response.status()
        );

        String jsonStrLogData = this.jsonTransformer.transformObject(logData);
        this.logger.Log(jsonStrLogData);
    };

    private class LogData {

        @Expose(deserialize = false)
        @SerializedName("method")
        private String method;

        @Expose(deserialize = false)
        @SerializedName("uri")
        private String uri;

        @Expose(deserialize = false)
        @SerializedName("request_body")
        private String requestBody;

        @Expose(deserialize = false)
        @SerializedName("response_body")
        private String responseBody;

        @Expose(deserialize = false)
        @SerializedName("status_code")
        private int statusCode;

        private LogData(String method, String uri, String requestBody, String responseBody, int statusCode) {
            this.method = method;
            this.uri = uri;
            this.requestBody = requestBody;
            this.responseBody = responseBody;
            this.statusCode = statusCode;
        }
    }
}
