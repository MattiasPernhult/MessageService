package app.component.message;

import app.Constant;
import app.http.exception.ApiException;
import app.http.parser.JsonParser;
import app.http.transformer.JsonTransformer;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class MessageHandler {

    private MessageServiceFactory messageServiceFactory;
    private JsonTransformer jsonTransformer;
    private JsonParser jsonParser;

    public MessageHandler(
            MessageServiceFactory messageServiceFactory,
            JsonTransformer jsonTransformer,
            JsonParser jsonParser
    ) {
        this.messageServiceFactory = messageServiceFactory;
        this.jsonTransformer = jsonTransformer;
        this.jsonParser = jsonParser;
    }

    public Route createMessage = (Request request, Response response) -> {

        try {
            Message message = this.jsonParser.parseBodyOfCreateMessageRequest(request);

            MessageService messageService = this.messageServiceFactory.create();
            messageService.create(message);

            response.status(200);
            return this.jsonTransformer.transformMessage(message);

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

    public Route updateMessage = (Request request, Response response) -> {

        try {
            Message message = this.jsonParser.parseBodyOfUpdateMessageRequest(request);

            MessageService messageService = this.messageServiceFactory.create();
            messageService.update(message);

            response.status(200);
            return this.jsonTransformer.transformMessage(message);

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

    public Route deleteMessage = (Request request, Response response) -> {

        try {
            String userId = request.params(Constant.USER_ID_PARAM);
            String messageId = request.params(Constant.MESSAGE_ID_PARAM);

            MessageService messageService = this.messageServiceFactory.create();
            messageService.delete(userId, messageId);

            response.status(204);
            return "";

        } catch (ApiException apiException) {
            response.status(apiException.httpStatusCode());
            return this.jsonTransformer.transformApiException(apiException);

        } catch (Exception e) {
            ApiException apiException = ApiException.Unknown.setDetail(e.getMessage());
            response.status(apiException.httpStatusCode());
            return this.jsonTransformer.transformApiException(apiException);
        }
    };

    public Route getAllMessages = (Request request, Response response) -> {

        try {
            MessageService messageService = this.messageServiceFactory.create();
            List<Message> messages = messageService.getAll();

            response.status(200);
            return this.jsonTransformer.transformMessages(messages);

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
