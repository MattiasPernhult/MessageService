package app.http.transformer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

class ApiDateTimeResponse {

    @Expose(deserialize = false)
    @SerializedName("timestamp")
    private Long timestamp;

    @Expose(deserialize = false)
    @SerializedName("offset")
    private Integer offset;

    private ApiDateTimeResponse(Long timestamp, Integer offset) {
        this.timestamp = timestamp;
        this.offset = offset;
    }

    static ApiDateTimeResponse fromDateTime(DateTime dateTime) {
        return new ApiDateTimeResponse(
                dateTime.getMillis(),
                dateTime.getZone().toTimeZone().getRawOffset() / 1000
        );
    }

}
