package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.model.SatelliteDto;
import com.smousseur.orbitrack.api.repository.SatelliteRepository;
import com.smousseur.orbitrack.model.entity.Satellite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SatelliteService {
  private final SatelliteRepository satelliteRepository;

  public Mono<SatelliteDto> getSatelliteDetails(Integer orbitalObjectId, String satelliteName) {
    return satelliteRepository
        .findByOrbitalBodyId(orbitalObjectId)
        .map(satellite -> this.mapSatelliteDto(satellite, satelliteName));
  }

  private SatelliteDto mapSatelliteDto(Satellite satellite, String satelliteName) {
    SatelliteDto satelliteDto = SatelliteDto.fromEntity(satellite);
    satelliteDto.setName(satelliteName);
    return satelliteDto;
  }
}
