package com.smousseur.orbitrack.api.repository;

import com.smousseur.orbitrack.api.model.OrbitalBodySearchResponse;
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
                SELECT ob.id AS id, ob.norad_id as norad_id,
                       ob.name AS name, ob.object_id AS cospar_id,
                       ob."object_type" AS object_type,
                       ob.size AS size,
                       ob.country_code AS country_code,
                       ob.launch_date AS launch_date
                FROM orbital_bodies ob
                WHERE (:noradId IS NULL
                       OR ob.norad_id = :noradId)
                  AND (:likeName IS NULL
                       OR ob.name LIKE :likeName)
                  AND (:cosparId IS NULL
                       OR ob.object_id = :cosparId)
                  AND (:type IS NULL
                       OR ob.object_type = :type)
                  AND (:countryCode IS NULL
                       OR ob.country_code = :countryCode)
                LIMIT :limit OFFSET :offset
            """)
  Flux<OrbitalBodySearchResponse> search(
      @Param("noradId") Integer noradId,
      @Param("likeName") String likeName,
      @Param("cosparId") String cosparId,
      @Param("type") String type,
      @Param("countryCode") String countryCode,
      @Param("limit") int limit,
      @Param("offset") int offset);

  @Query(
      """
                SELECT count(*)
                FROM orbital_bodies ob
                LEFT JOIN satellites sat ON ob.id = sat.orbital_body_id
                WHERE (:noradId IS NULL
                       OR ob.norad_id = :noradId)
                  AND (:likeName IS NULL
                       OR ob.name LIKE :likeName)
                  AND (:cosparId IS NULL
                       OR ob.object_id = :cosparId)
                  AND (:type IS NULL
                       OR ob.object_type = :type)
                  AND (:countryCode IS NULL
                       OR ob.country_code = :countryCode)
                """)
  Mono<Integer> count(
      @Param("noradId") Integer noradId,
      @Param("likeName") String likeName,
      @Param("cosparId") String cosparId,
      @Param("type") String type,
      @Param("countryCode") String countryCode);
}
