package app.http.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiException extends Exception {

    @Expose(deserialize = false)
    @SerializedName("code")
    private int code;

    @Expose(deserialize = false)
    @SerializedName("reason")
    private String reason;

    @Expose(deserialize = false)
    @SerializedName("detail")
    private String detail;

    private ApiException(int code, String reason) {
        super(reason);
        this.code = code;
        this.reason = reason;
    }

    public ApiException setDetail(String detail) {
        ApiException copy = this;
        copy.detail = detail;
        return copy;
    }

    public int httpStatusCode() {
        if (this.code >= Unauthorized.code && this.code < BadRequest.code) {
            return 401;
        }

        if (this.code >= BadRequest.code && this.code < ResourceNotFound.code) {
            return 400;
        }

        if (this.code >= ResourceNotFound.code && this.code < ServerError.code) {
            return 404;
        }

        if (this.code == ServerError.code) {
            return 500;
        }

        return 422;
    }

    public static ApiException Unknown = new ApiException(1000, "Unknown");

    public static ApiException Unauthorized = new ApiException(2000, "Unauthorized");
    public static ApiException InvalidToken = new ApiException(2001, "Invalid Token");

    public static ApiException BadRequest = new ApiException(3000, "Bad Request");
    public static ApiException ParameterMissing = new ApiException(3001, "Parameter Missing");
    public static ApiException ParameterInvalid = new ApiException(3002, "Parameter Invalid");
    public static ApiException JsonInvalid = new ApiException(3003, "JSON Invalid");
    public static ApiException UserAlreadyExists = new ApiException(3004, "User already exists");
    public static ApiException UserNotAllowedToModifyMessage = new ApiException(3005, "User not allowed to update or delete message of another user");
    public static ApiException AuthorizationTokenMalformed = new ApiException(3006, "Authorization token malformed");

    public static ApiException ResourceNotFound = new ApiException(4000, "Resource not found");
    public static ApiException TokenNotFound = new ApiException(4001, "Token not found");
    public static ApiException UserNotFound = new ApiException(4002, "User not found");
    public static ApiException MessageNotFound = new ApiException(4003, "Message not found");


    public static ApiException ServerError = new ApiException(5000, "Server Error");
}
