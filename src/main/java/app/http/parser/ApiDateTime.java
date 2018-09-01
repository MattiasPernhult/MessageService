package app.http.parser;

import app.http.exception.ApiException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

class ApiDateTime {

    @Expose(serialize = false)
    @SerializedName("timestamp")
    private Long timestamp;

    @Expose(serialize = false)
    @SerializedName("offset")
    private Integer offset;

    void validate(String name) throws ApiException {
        if (this.timestamp == null) {
            throw ApiException.ParameterMissing.setDetail(String.format("%s.timestamp", name));
        }

        if (!this.isTimestampValid()) {
            throw ApiException.ParameterInvalid.setDetail(String.format("%s.timestamp", name));
        }

        if (this.offset == null) {
            throw ApiException.ParameterMissing.setDetail(String.format("%s.offset", name));
        }

        if (!this.isOffsetValid()) {
            throw ApiException.ParameterInvalid.setDetail(String.format("%s.offset", name));
        }
    }

    DateTime convert() {
        return new DateTime(
                this.timestamp,
                DateTimeZone.forOffsetHoursMinutes(
                        this.getOffsetInHours(),
                        this.getOffsetInMinutes()
                )
        );
    }

    private boolean isTimestampValid() {
        return timestamp != null &&
                timestamp > 0;
    }

    private boolean isOffsetValid() {
        int MIN_OFFSET_SECONDS = -43200;
        int MAX_OFFSET_SECONDS = 50400;

        return offset != null &&
                offset <= MAX_OFFSET_SECONDS &&
                offset >= MIN_OFFSET_SECONDS;
    }

    private int getOffsetInHours() {
        if (this.offset == 0) {
            return this.offset;
        }

        return this.offset / 3600;
    }

    private int getOffsetInMinutes() {
        if (this.offset == 0) {
            return this.offset;
        }

        return this.offset % 3600 / 60;
    }
}
