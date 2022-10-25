package ru.practicum.ewm.utills;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapper {

    private DateTimeMapper() {
    }

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static DateTimeFormatter formatter;

    public static LocalDateTime toDateTime(String str) {
        formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(str, formatter);
    }

    public static String toString(LocalDateTime date) {
        formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return date.format(formatter);
    }
}
