package app;

import app.component.message.MessageHandler;
import app.component.message.MessageServiceFactory;
import app.component.message.MessageServiceMemoryDBFactory;
import app.component.token.TokenHandler;
import app.component.token.TokenServiceFactory;
import app.component.token.TokenServiceMemoryDBFactory;
import app.component.user.UserHandler;
import app.component.user.UserServiceFactory;
import app.component.user.UserServiceMemoryDBFactory;
import app.db.MemoryDB;
import app.http.exception.ApiException;
import app.http.middleware.AuthMiddleware;
import app.http.middleware.ContentTypeMiddleware;
import app.http.middleware.CorsMiddleware;
import app.http.middleware.LoggerMiddleware;
import app.http.parser.JsonParser;
import app.http.transformer.JsonTransformer;
import app.service.jwt.Jwt;
import app.service.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

class App {

    private Logger logger;

    private String contentTypeValue;
    private CorsMiddleware.Data corsData;

    private UserServiceFactory userServiceFactory;
    private TokenServiceFactory tokenServiceFactory;
    private MessageServiceFactory messageServiceFactory;

    private JsonTransformer jsonTransformer;
    private JsonParser jsonParser;

    App(
            MemoryDB memoryDB,
            Jwt jwt,
            Logger logger,
            String contentTypeValue,
            String corsOrigins,
            String corsMethods,
            String corsHeaders
    ) {
        this.logger = logger;

        this.contentTypeValue = contentTypeValue;
        this.corsData = CorsMiddleware.createData(corsOrigins, corsMethods, corsHeaders);

        this.userServiceFactory = new UserServiceMemoryDBFactory(memoryDB);
        this.tokenServiceFactory = new TokenServiceMemoryDBFactory(memoryDB, jwt);
        this.messageServiceFactory = new MessageServiceMemoryDBFactory(memoryDB);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        this.jsonTransformer = new JsonTransformer(gson);
        this.jsonParser = new JsonParser(gson);
    }

    App build(int port) {
        port(port);
        this.buildRoutes();
        return this;
    }

    void start() {
    }

    private void buildRoutes() {
        ContentTypeMiddleware contentTypeMiddleware = new ContentTypeMiddleware(
                this.contentTypeValue,
                this.jsonTransformer
        );

        CorsMiddleware corsMiddleware = new CorsMiddleware(this.corsData);

        LoggerMiddleware loggerMiddleware = new LoggerMiddleware(this.logger, this.jsonTransformer);

        AuthMiddleware authMiddleware = new AuthMiddleware(
                this.tokenServiceFactory,
                this.jsonTransformer
        );

        UserHandler userHandler = new UserHandler(
                this.userServiceFactory,
                this.jsonTransformer,
                this.jsonParser
        );
        TokenHandler tokenHandler = new TokenHandler(
                this.tokenServiceFactory,
                this.jsonTransformer,
                this.jsonParser
        );
        MessageHandler messageHandler = new MessageHandler(
                this.messageServiceFactory,
                this.jsonTransformer,
                this.jsonParser
        );

        path("/api", () -> {
            before("/*",
                    contentTypeMiddleware.middleware,
                    corsMiddleware.middleware
            );

            before("/users/*",
                    contentTypeMiddleware.ensureCorrectRequestContentType
            );

            path("/v1", () -> {
                // Create user endpoint
                post("/users", userHandler.createUser);

                // Create token endpoint
                post(String.format("/users/:%s/tokens", Constant.USER_ID_PARAM), tokenHandler.createToken);

                // Get all messages endpoint
                get("/messages", messageHandler.getAllMessages);

                // Create message endpoint
                before(String.format("/users/:%s/messages", Constant.USER_ID_PARAM), authMiddleware.authenticateUser);
                post(String.format("/users/:%s/messages", Constant.USER_ID_PARAM), messageHandler.createMessage);

                // Update message endpoint
                before(String.format(
                        "/users/:%s/messages/:%s",
                        Constant.USER_ID_PARAM,
                        Constant.MESSAGE_ID_PARAM
                ), authMiddleware.authenticateUser);
                put(String.format(
                        "/users/:%s/messages/:%s",
                        Constant.USER_ID_PARAM,
                        Constant.MESSAGE_ID_PARAM
                ), messageHandler.updateMessage);

                // Delete message endpoint
                delete(String.format(
                        "/users/:%s/messages/:%s",
                        Constant.USER_ID_PARAM,
                        Constant.MESSAGE_ID_PARAM
                ), messageHandler.deleteMessage);
            });

            post("/*", notFound);
            get("/*", notFound);
            put("/*", notFound);
            delete("/*", notFound);

            after("/*", loggerMiddleware.middleware);
        });
    }

    private static Route notFound = (Request request, Response response) -> {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        response.status(404);
        return new JsonTransformer(gson).transformApiException(ApiException.ResourceNotFound);
    };
}
