package app.http.middleware;

import spark.Filter;
import spark.Request;
import spark.Response;

public class CorsMiddleware {

    private Data data;

    private final String CORS_ORIGINS_HEADER = "Access-Control-Allow-Origin";
    private final String CORS_METHODS_HEADER = "Access-Control-Allow-Methods";
    private final String CORS_HEADERS_HEADER = "Access-Control-Allow-Headers";

    public CorsMiddleware(Data data) {
        this.data = data;
    }

    public Filter middleware = (Request request, Response response) -> {
        response.header(CORS_ORIGINS_HEADER, this.data.origins);
        response.header(CORS_METHODS_HEADER, this.data.methods);
        response.header(CORS_HEADERS_HEADER, this.data.headers);
    };

    public static Data createData(String origins, String methods, String headers) {
        return new Data(origins, methods, headers);
    }

    public static class Data {
        private String origins;
        private String methods;
        private String headers;

        private Data(String origins, String methods, String headers) {
            this.origins = origins;
            this.methods = methods;
            this.headers = headers;
        }
    }
}
