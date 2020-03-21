package com.mohsinkerai.adminlte.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class StatusMapperUtils {

  private static Map<String, String> recommendationToStatusMap = ImmutableMap
    .<String, String>builder()
    .put("Stable and discharged", "Stable")
    .put("Isolation at Home", "Home Quarantine")
    .put("Refer to Hospital", "Refer to Hospital")
    .build();

  public static String getStatus(String recommendation) {
    return recommendationToStatusMap.get(recommendation);
  }
}
