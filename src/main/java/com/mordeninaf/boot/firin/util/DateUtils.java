package com.mordeninaf.boot.firin.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String TIME_PATTERN = "dd.MM.yyyy HH:mm";
    private static final String TR_LANG = "tr-TR";

    private DateUtils() {}

    public static String toTurkishDateTime(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.forLanguageTag(TR_LANG));
            return dateTimeFormatter.format(localDateTime);
        } else {
            return "";
        }
    }

    public static String toTurkishDate(LocalDate localDate) {
        if (localDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.forLanguageTag(TR_LANG));
            return dateTimeFormatter.format(localDate);
        } else {
            return "";
        }
    }

    public static String toTurkishDateFromIso(String localDateIso) {
        if (localDateIso != null) {
            LocalDate localDate = LocalDate.parse(localDateIso, DateTimeFormatter.ISO_DATE);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.forLanguageTag(TR_LANG));
            return dateTimeFormatter.format(localDate);
        } else {
            return "";
        }
    }

    public static String toIsoDate(LocalDate localDate) {
        if (localDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
            return dateTimeFormatter.format(localDate);
        } else {
            return "";
        }
    }

    public static String toIsoDate(String localDate) {
        if (localDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
            return LocalDate.parse(localDate, dateTimeFormatter).toString();
        } else {
            return "";
        }
    }

    public static String toTurkishDateFromNow() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.forLanguageTag(TR_LANG));
        return dateTimeFormatter.format(LocalDate.now(ZoneId.systemDefault()));
    }

    public static String toIsoDateFromNow() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        return dateTimeFormatter.format(LocalDate.now(ZoneId.systemDefault()));
    }
}
