package com.smousseur.orbitrack.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GeoPosition {
  private double latitude;
  private double longitude;
  private double altitude;
  private double speed;
}
