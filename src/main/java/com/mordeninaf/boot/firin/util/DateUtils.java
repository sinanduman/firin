package com.mordeninaf.boot.firin.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    public static String toTurkishDateTime(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.forLanguageTag("tr-TR"));
            return dateTimeFormatter.format(localDateTime);
        } else {
            return "";
        }
    }

    public static String toTurkishDate(LocalDate localDate) {
        if (localDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("tr-TR"));
            return dateTimeFormatter.format(localDate);
        } else {
            return "";
        }
    }

    public static String toTurkishDateFromIso(String localDateIso) {
        if (localDateIso != null) {
            LocalDate localDate = LocalDate.parse(localDateIso, DateTimeFormatter.ISO_DATE);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("tr-TR"));
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("tr-TR"));
        return dateTimeFormatter.format(LocalDate.now(ZoneId.systemDefault()));
    }

    public static String toIsoDateFromNow() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        return dateTimeFormatter.format(LocalDate.now(ZoneId.systemDefault()));
    }
}
