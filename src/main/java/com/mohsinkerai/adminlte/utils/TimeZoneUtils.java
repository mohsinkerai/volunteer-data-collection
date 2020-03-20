package com.mohsinkerai.adminlte.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeZoneUtils {

  public static String convertTimeZone(LocalDateTime localDateTime, String pattern, String zone) {
    return DateTimeFormatter.ofPattern(pattern)
      .withZone(ZoneId.of(zone))
      .format(
        localDateTime.atZone(ZoneId.systemDefault()).toInstant()
      );
  }
}
