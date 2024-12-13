package com.smousseur.orbitrack.referential.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CookieDto {
  private String name;
  private String value;
}
