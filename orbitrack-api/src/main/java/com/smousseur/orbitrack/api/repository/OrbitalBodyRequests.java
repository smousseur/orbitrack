package com.smousseur.orbitrack.api.repository;

public final class OrbitalBodyRequests {
  public static final String COUNT_CLAUSE = "SELECT count(*) as row_count ";
  public static final String SELECT_CLAUSE =
      """
        SELECT ob.name AS name,
               ob."object_type" AS object_type,
               ob.size AS size,
               ob.country_code AS country_code,
               ob.launch_date AS launch_date,
               sat.kind AS kind
        """;
  public static final String FROM_CLAUSE =
      """
        FROM orbital_bodies ob
        LEFT JOIN satellites sat ON ob.id = sat.orbital_body_id
        """;
  public static final String WHERE_CLAUSE =
      """
        WHERE (:likeName IS NULL
               OR ob.name like :likeName)
          AND (:type IS NULL
               OR ob.object_type = :type)
        """;
}
