package com.smousseur.orbitrack.api.repository;

import com.smousseur.orbitrack.model.OrbitalBodySearchResponse;
import com.smousseur.orbitrack.model.entity.OrbitalBody;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrbitalBodyRepository extends ReactiveCrudRepository<OrbitalBody, Integer> {
  Flux<OrbitalBody> findByNameContains(String name);

  @Query(
      """
            SELECT ob.name AS name,
                   ob.object_type AS TYPE,
                   ob.size AS SIZE,
                   ob.country_code AS countryCode,
                   ob.launch_date AS launchDate,
                   sat.type AS FUNCTION
            FROM orbital_bodies ob
            LEFT JOIN satellites sat ON ob.id = sat.orbital_body_id
            WHERE (:likeName IS NULL
                   OR ob.name like :likeName)
              AND (:type IS NULL
                   OR ob.object_type = :type)
            LIMIT :limit
            OFFSET :offset
        """)
  Flux<OrbitalBodySearchResponse> search(
      @Param("likeName") String likeName,
      @Param("type") String type,
      @Param("limit") int limit,
      @Param("offset") int offset);

  @Query(
      """
            SELECT count(*)
            FROM orbital_bodies ob
            LEFT JOIN satellites sat ON ob.id = sat.orbital_body_id
            WHERE (:likeName IS NULL
                   OR ob.name like :likeName)
              AND (:type IS NULL
                   OR ob.object_type = :type)
            """)
  Mono<Integer> count(@Param("likeName") String likeName, @Param("type") String type);
}
