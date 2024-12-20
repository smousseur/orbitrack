package com.smousseur.orbitrack.api.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrbitalBodyPosition {
  private GeoPosition position;
  private Instant timeOfMeasure;
}
