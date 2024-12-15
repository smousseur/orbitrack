package com.smousseur.orbitrack.api.model;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrbitalBodyType {
  DEBRIS("DEBRIS"),
  PAYLOAD("PAYLOAD"),
  ROCKET_BODY("ROCKET BODY"),
  UNKNOWN("UNKNOWN");

  final String value;

  public static OrbitalBodyType fromValue(String value) {
    return Arrays.stream(OrbitalBodyType.values())
        .filter(e -> e.value.equals(value))
        .findFirst()
        .orElse(null);
  }
}
