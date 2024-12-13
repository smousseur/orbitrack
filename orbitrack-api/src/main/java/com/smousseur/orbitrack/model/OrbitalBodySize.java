package com.smousseur.orbitrack.model;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrbitalBodySize {
  LARGE("LARGE"),
  MEDIUM("MEDIUM"),
  SMALL("SMALL");

  final String value;

  public static OrbitalBodySize fromValue(String value) {
    return Arrays.stream(OrbitalBodySize.values())
        .filter(e -> e.value.equals(value))
        .findFirst()
        .orElse(null);
  }
}
