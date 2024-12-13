package com.smousseur.orbitrack.scraper.service;

import com.smousseur.orbitrack.model.entity.Satellite;
import com.smousseur.orbitrack.scraper.model.ScrapSatellite;
import org.springframework.stereotype.Service;

@Service
public class MapperService {
  public Satellite map(ScrapSatellite satellite) {
    return Satellite.builder()
        .orbitalBodyId(satellite.getOrbitalBodyId())
        .objectId(satellite.getCospar())
        .kind(satellite.getType())
        .operator(satellite.getOperator())
        .contractors(satellite.getContractors())
        .equipment(satellite.getEquipment())
        .configuration(satellite.getConfiguration())
        .propulsion(satellite.getPropulsion())
        .power(satellite.getPower())
        .lifetime(satellite.getLifetime())
        .mass(satellite.getMass())
        .orbit(satellite.getOrbit())
        .hasImage(satellite.getImage() != null)
        .build();
  }
}
