package com.smousseur.orbitrack.scraper.repository;

import com.smousseur.orbitrack.model.entity.Satellite;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteRepository extends ReactiveCrudRepository<Satellite, Integer> {}
