package com.smousseur.orbitrack.model;

import com.smousseur.orbitrack.model.entity.OrbitalBody;
import lombok.Data;

@Data
public class OrbitalBodySearchFastResponse {
  private String objectId;
  private String label;

  public OrbitalBodySearchFastResponse(OrbitalBody object) {
    this.objectId = String.valueOf(object.getId());
    this.label = object.getName() + " (" + object.getObjectId() + ")";
  }
}
