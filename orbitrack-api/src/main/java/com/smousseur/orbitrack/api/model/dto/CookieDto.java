package com.smousseur.orbitrack.api.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CookieDto {
  private String name;
  private String value;
}
