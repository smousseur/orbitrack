package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.model.GeoPosition;
import com.smousseur.orbitrack.model.OrbitalBodyPosition;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.springframework.stereotype.Service;

@Service
public class OrbitalBodySpeedService {
  private final Map<Integer, OrbitalBodyPosition> positions = new HashMap<>();

  public void updatePosition(Integer objectId, OrbitalBodyPosition position) {
    positions.compute(objectId, (key, value) -> position);
  }

  public Optional<Double> computeSpeed(Integer objectId, GeoPosition newPosition, Instant atTime) {
    return Optional.ofNullable(positions.get(objectId))
        .map(pos -> computeSpeed(pos, newPosition, atTime));
  }

  private static Double computeSpeed(
      OrbitalBodyPosition oldPosition, GeoPosition newPosition, Instant atTime) {
    GeoPosition oldPos = oldPosition.getPosition();
    GeodeticPoint oldGeoPos =
        new GeodeticPoint(
            Math.toRadians(oldPos.getLatitude()),
            Math.toRadians(oldPos.getLongitude()),
            oldPos.getAltitude());
    GeodeticPoint newGeoPos =
        new GeodeticPoint(
            Math.toRadians(newPosition.getLatitude()),
            Math.toRadians(newPosition.getLongitude()),
            newPosition.getAltitude());
    Vector3D vectorOld = OrekitService.getEarthModelForPositions().transform(oldGeoPos);
    Vector3D vectorNew = OrekitService.getEarthModelForPositions().transform(newGeoPos);
    double distance = Vector3D.distance(vectorOld, vectorNew);
    double seconds =
        Duration.between(oldPosition.getTimeOfMeasure(), atTime).toNanos() / 1_000_000_000.0;

    return distance / seconds;
  }
}
