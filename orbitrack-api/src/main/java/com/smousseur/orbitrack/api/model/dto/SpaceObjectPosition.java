package com.smousseur.orbitrack.api.model.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpaceObjectPosition {
  private GeoPosition position;
  private Instant timeOfMeasure;
}
