package com.smousseur.orbitrack.scraper.service;

import com.smousseur.orbitrack.model.entity.OrbitalBody;
import com.smousseur.orbitrack.model.entity.Satellite;
import com.smousseur.orbitrack.scraper.exception.ScraperException;
import com.smousseur.orbitrack.scraper.model.ScrapSatellite;
import com.smousseur.orbitrack.scraper.repository.OrbitalBodyRepository;
import com.smousseur.orbitrack.scraper.repository.SatelliteRepository;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.graalvm.collections.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ScraperService {
  private static final String BASE_URL = "https://space.skyrocket.de/directories/";
  private static final String COSPAR_REGEX = "^\\d{4}-\\d{3}[A-Z]+$";
  private static final Pattern COSPAR_PATTERN = Pattern.compile(COSPAR_REGEX);

  private final OrbitalBodyRepository orbitalBodyRepository;
  private final ImageDownloaderService imageDownloaderService;
  private final MapperService mapperService;
  private final SatelliteRepository satelliteRepository;

  private final Set<String> processedSatelliteUrls = new HashSet<>();
  private final Set<String> processedSatellites = new HashSet<>();

  public void process() {
    getScrapSatellites().subscribe(satellite -> satelliteRepository.save(satellite).subscribe());
  }

  public Flux<Satellite> getScrapSatellites() {
    return computeSatelliteUrl()
        .flatMap(
            url -> {
              Document satelliteDocument = getSatelliteDocument(url);
              List<ScrapSatellite> satellites = getSatellites(satelliteDocument);
              return Flux.fromIterable(satellites);
            })
        .filterWhen(
            satellite -> {
              boolean result = !processedSatellites.contains(satellite.getCospar());
              processedSatellites.add(satellite.getCospar());
              return Mono.just(result);
            })
        .flatMap(
            scrapSatellite ->
                orbitalBodyRepository
                    .findByObjectId(scrapSatellite.getCospar())
                    .map(satellite -> Pair.create(scrapSatellite, satellite)))
        .map(
            satellitePair -> {
              ScrapSatellite scrapSatellite = satellitePair.getLeft();
              if (scrapSatellite.getImage() != null) {
                imageDownloaderService.download(
                    BASE_URL + scrapSatellite.getImage(),
                    "C:\\Prog\\projects\\intelliJ\\orbitrack\\images\\"
                        + scrapSatellite.getCospar()
                        + ".jpg");
              }
              OrbitalBody orbitalBody = satellitePair.getRight();
              scrapSatellite.setOrbitalBodyId(orbitalBody.getId());
              return scrapSatellite;
            })
        .map(mapperService::map);
  }

  private Flux<String> computeSatelliteUrl() {
    return Mono.just(this.getRootDocument())
        .flatMapMany(this::getCountryFlux)
        .map(this::getCountryDocument)
        .flatMap(this::getSatelliteFlux)
        .map(satLink -> BASE_URL + satLink.attr("href").toLowerCase());
  }

  private List<ScrapSatellite> getSatellites(Document satelliteDoc) {
    List<ScrapSatellite> results = new ArrayList<>();
    String type = getCharacteristic(satelliteDoc, "sdtyp");
    String operator = getCharacteristic(satelliteDoc, "sdope");
    String contractors = getCharacteristic(satelliteDoc, "sdcon");
    String equipment = getCharacteristic(satelliteDoc, "sdequ");
    String configuration = getCharacteristic(satelliteDoc, "sdcnf");
    String propulsion = getCharacteristic(satelliteDoc, "sdpro");
    String power = getCharacteristic(satelliteDoc, "sdpow");
    String lifetime = getCharacteristic(satelliteDoc, "sdlif");
    String mass = getCharacteristic(satelliteDoc, "sdmas");
    String orbit = getCharacteristic(satelliteDoc, "sdorb");
    String image = getImage(satelliteDoc).orElse(null);
    Element satelliteTable = satelliteDoc.selectFirst("table[id^='satlist']");
    Optional<Elements> satellitesElements =
        Optional.ofNullable(satelliteTable)
            .map(table -> table.selectFirst("tr"))
            .map(Element::siblingElements);
    satellitesElements.ifPresent(
        satellites ->
            satellites.forEach(
                satellite -> {
                  Element cosparElement = satellite.selectFirst("td[class='cosid']");
                  if (cosparElement != null) {
                    String cospar = cosparElement.text();
                    if (isCosparValid(cospar)) {
                      results.add(
                          ScrapSatellite.from(
                              type,
                              operator,
                              contractors,
                              equipment,
                              configuration,
                              propulsion,
                              power,
                              lifetime,
                              mass,
                              orbit,
                              cospar,
                              image));
                    }
                  }
                }));
    return results;
  }

  private Optional<String> getImage(Document satelliteDoc) {
    Element imageContainer = satelliteDoc.selectFirst("div[id='contimg']");
    return Optional.ofNullable(imageContainer)
        //        .map(imgContainer -> imgContainer.selectFirst("img"))
        .map(imgContainer -> imgContainer.select("img"))
        .map(Elements::last)
        .map(img -> img.attr("src"));
  }

  private String getCharacteristic(Document satelliteDoc, String tdId) {
    Element typeElement = satelliteDoc.selectFirst("td[id='" + tdId + "']");
    return Optional.ofNullable(typeElement).map(Element::text).orElse("N/A");
  }

  private Document getSatelliteDocument(String satelliteurl) {
    try {
      return Jsoup.connect(satelliteurl).get();
    } catch (IOException e) {
      throw new ScraperException(e);
    }
  }

  private Document getRootDocument() {
    try {
      return Jsoup.connect(BASE_URL + "sat.htm").get();
    } catch (IOException e) {
      throw new ScraperException(e);
    }
  }

  private Flux<Element> getSatelliteFlux(Document countryDoc) {
    Elements elements = countryDoc.select("a[href^='../doc_sdat']");

    elements =
        elements.stream()
            .filter(element -> !processedSatelliteUrls.contains(element.attr("href").toLowerCase()))
            .collect(Collectors.toCollection(Elements::new));
    elements.forEach(element -> processedSatelliteUrls.add(element.attr("href").toLowerCase()));
    return Flux.fromIterable(elements);
  }

  private Document getCountryDocument(Element countryLink) {
    String countryUrl = BASE_URL + countryLink.attr("href").toLowerCase();
    try {
      return Jsoup.connect(countryUrl).get();
    } catch (IOException e) {
      throw new ScraperException(e);
    }
  }

  private Flux<Element> getCountryFlux(Document rootDoc) {
    Elements links = rootDoc.select("a[href^='sat_']");
    return Flux.fromIterable(links);
  }

  public static boolean isCosparValid(String cosparId) {
    return COSPAR_PATTERN.matcher(cosparId).matches();
  }
}
