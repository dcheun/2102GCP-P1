package dev.cheun.utils;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateTimeUtil {
    public static OffsetDateTime getOffsetDateTimeUtcNow() {
        return OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public static Timestamp getTimestampUtcNow() {
        return offsetDateTimeToTimestamp(getOffsetDateTimeUtcNow());
    }

    public static OffsetDateTime timestampToOffsetDateTime(Timestamp ts) {
        return OffsetDateTime.ofInstant(ts.toInstant(), ZoneId.of("UTC"));
    }

    public static Timestamp offsetDateTimeToTimestamp(OffsetDateTime odt) {
        return Timestamp.valueOf(odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }
}
