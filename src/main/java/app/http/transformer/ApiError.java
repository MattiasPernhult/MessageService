package app.http.transformer;

import app.http.exception.ApiException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ApiError {

    @Expose(deserialize = false)
    @SerializedName("error")
    private ApiException error;

    private ApiError(ApiException error) {
        this.error = error;
    }

    static ApiError fromApiException(ApiException apiException) {
        return new ApiError(apiException);
    }
}
