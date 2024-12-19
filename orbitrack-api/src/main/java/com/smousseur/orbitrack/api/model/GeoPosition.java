package com.smousseur.orbitrack.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GeoPosition {
  private Integer objectId;
  private String objectName;
  private double latitude;
  private double longitude;
  private double altitude;
  private double speed;
}
