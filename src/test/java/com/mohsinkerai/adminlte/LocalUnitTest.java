package com.mohsinkerai.adminlte;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LocalUnitTest {

  @Test
  public void helloTest() {
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    System.out.println(now.toString());

    java.time.LocalDateTime.now();

    String date = java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm").withZone(java.time.ZoneId.of("America/New_York")).format(java.time.LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant());
    System.out.println(date);
  }
}
