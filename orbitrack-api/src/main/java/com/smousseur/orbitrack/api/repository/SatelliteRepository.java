package com.smousseur.orbitrack.api.repository;

import com.smousseur.orbitrack.model.entity.Satellite;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SatelliteRepository extends ReactiveCrudRepository<Satellite, Integer> {
  Mono<Satellite> findByOrbitalBodyId(Integer orbitalBodyId);
}
