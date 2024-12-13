package com.smousseur.orbitrack.model;

import java.time.LocalDate;

public interface OrbitalBodySearchResponse {
  String getName();

  String type();

  String size();

  String countryCode();

  LocalDate launchDate();

  String function();
}
