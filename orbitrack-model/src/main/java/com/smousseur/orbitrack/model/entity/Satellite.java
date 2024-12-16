package com.smousseur.orbitrack.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("satellites")
public class Satellite {
  @Id private Integer id;

  @Column("orbital_body_id")
  private Integer orbitalBodyId;

  @Column("object_id")
  private String objectId;

  private String kind;
  private String operator;
  private String contractors;
  private String equipment;
  private String configuration;
  private String propulsion;
  private String power;
  private String lifetime;
  private String mass;
  private String orbit;

  @Column("has_image")
  private Boolean hasImage;
}
