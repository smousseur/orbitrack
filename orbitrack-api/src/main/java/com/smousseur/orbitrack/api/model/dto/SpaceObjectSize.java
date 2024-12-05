package com.smousseur.orbitrack.api.model.dto;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpaceObjectSize {
  LARGE("LARGE"),
  MEDIUM("MEDIUM"),
  SMALL("SMALL");

  final String value;

  public static SpaceObjectSize fromValue(String value) {
    return Arrays.stream(SpaceObjectSize.values())
        .filter(e -> e.value.equals(value))
        .findFirst()
        .orElse(null);
  }
}
