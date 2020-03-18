package com.mohsinkerai.adminlte;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
public class LocalUnitTest {

  @Test
  public void helloTest() {
    LocalDateTime now = LocalDateTime.now();
    System.out.println(now.toString());

    String date = java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm").withZone(java.time.ZoneId.of("America/New_York")).format(now.atZone(java.time.ZoneId.systemDefault()).toInstant());
    System.out.println(date);
  }
}
