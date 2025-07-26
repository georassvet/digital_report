package ru.riji.comparator.helpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static String convertToUtc(String dateTime) {
        LocalDateTime ldt = LocalDateTime.parse(dateTime);
        ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
        return ldtZoned.withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
    }

    public static long convertToUtcMillis(LocalDateTime dateTime) {
        ZonedDateTime dateTimeInMyZone = ZonedDateTime.
                of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone
                .withZoneSameInstant(ZoneOffset.UTC)
                .toInstant().toEpochMilli();

    }
}
