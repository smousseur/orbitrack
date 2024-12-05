package com.smousseur.orbitrack.api.model.dto;

import com.smousseur.orbitrack.api.model.entity.SpaceObject;
import lombok.Data;

@Data
public class SpaceObjectSearchResponse {
  private String objectId;
  private String label;

  public SpaceObjectSearchResponse(SpaceObject object) {
    this.objectId = String.valueOf(object.getId());
    this.label = object.getName() + " (" + object.getObjectId() + ")";
  }
}
