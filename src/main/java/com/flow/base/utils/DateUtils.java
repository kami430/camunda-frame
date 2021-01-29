package com.flow.base.utils;

import java.time.*;
import java.util.Date;

public class DateUtils {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long asTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long asTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate asLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate startOfMonth(LocalDate localDate) {
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }

    public static LocalDate endOfMonth(LocalDate localDate) {
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getMonth().length(localDate.isLeapYear()));
    }

    public static LocalDateTime startOfMonth(LocalDateTime localDateTime) {
        return LocalDateTime.of(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), 1), LocalTime.MIN);
    }

    public static LocalDateTime endOfMonth(LocalDateTime localDateTime) {
        return LocalDateTime.of(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getMonth().length(Year.isLeap(localDateTime.getYear()))), LocalTime.MAX);
    }

    public static LocalDate startOfQuarter(LocalDate localDate) {
        return LocalDate.of(localDate.getYear(), localDate.getMonth().firstMonthOfQuarter(), 1);
    }

    public static LocalDate endOfQuarter(LocalDate localDate) {
        Month endMonthOfQuarter = Month.of(localDate.getMonth().firstMonthOfQuarter().getValue() + 2);
        return LocalDate.of(localDate.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(localDate.isLeapYear()));
    }

    public static LocalDateTime startOfQuarter(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth().firstMonthOfQuarter(), 1, 0, 0, 0, 0);
    }

    public static LocalDateTime endOfQuarter(LocalDateTime localDateTime) {
        Month endMonthOfQuarter = Month.of(localDateTime.getMonth().firstMonthOfQuarter().getValue() + 2);
        return LocalDateTime.of(localDateTime.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(Year.isLeap(localDateTime.getYear())), LocalTime.MAX.getHour(), LocalTime.MAX.getMinute(), LocalTime.MAX.getSecond(), LocalTime.MAX.getNano());
    }
}