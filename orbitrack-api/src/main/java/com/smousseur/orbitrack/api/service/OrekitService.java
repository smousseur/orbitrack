package com.smousseur.orbitrack.api.service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import lombok.Getter;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.ZipJarCrawler;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.ITRFVersion;
import org.orekit.models.earth.ReferenceEllipsoid;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;
import org.springframework.stereotype.Service;

@Service
public class OrekitService {
  @Getter private static ReferenceEllipsoid earthModelForPositions;

  @PostConstruct
  public void setup() {
    DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    manager.addProvider(new ZipJarCrawler(OrekitService.class.getClassLoader(), "orekit-data.zip"));
    initEarth();
  }

  private static void initEarth() {
    OrekitService.earthModelForPositions =
        ReferenceEllipsoid.getWgs84(
            FramesFactory.getITRF(ITRFVersion.ITRF_2020, IERSConventions.IERS_2010, true));
  }

  public GeodeticPoint computePosition(String tle1, String tle2, Instant atTime) {
    TLE tle = new TLE(tle1, tle2);
    TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
    AbsoluteDate date = new AbsoluteDate(atTime);
    PVCoordinates pvCoordinates = propagator.getPVCoordinates(date, FramesFactory.getTEME());
    return earthModelForPositions.transform(
        pvCoordinates.getPosition(), FramesFactory.getTEME(), date);
  }
}
