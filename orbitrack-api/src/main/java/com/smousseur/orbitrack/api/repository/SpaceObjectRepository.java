package com.smousseur.orbitrack.api.repository;

import com.smousseur.orbitrack.api.model.entity.SpaceObject;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SpaceObjectRepository extends ReactiveCrudRepository<SpaceObject, Integer> {
  Mono<SpaceObject> findByNoradId(Integer noradId);

  Flux<SpaceObject> findByNameContains(String name);
}
