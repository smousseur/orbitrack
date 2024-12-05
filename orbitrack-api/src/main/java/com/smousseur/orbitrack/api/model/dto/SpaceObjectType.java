package com.smousseur.orbitrack.api.model.dto;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpaceObjectType {
  DEBRIS("DEBRIS"),
  PAYLOAD("PAYLOAD"),
  ROCKET_BODY("ROCKET BODY"),
  UNKNOWN("UNKNOWN");

  final String value;

  public static SpaceObjectType fromValue(String value) {
    return Arrays.stream(SpaceObjectType.values())
            .filter(e -> e.value.equals(value))
            .findFirst()
            .orElse(null);
  }

}
