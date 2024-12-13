package com.smousseur.orbitrack.scraper.service;

import com.smousseur.orbitrack.scraper.exception.ScraperException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class ImageDownloaderService {
  private static final Logger logger = LoggerFactory.getLogger(ImageDownloaderService.class);
  private final WebClient webClient;
  private static final Scheduler scheduler = Schedulers.boundedElastic();

  public void download(String imageUrl, String destinationPath) {
    webClient
        .get()
        .uri(imageUrl)
        .retrieve()
        .onStatus(
            HttpStatusCode::isError,
            response -> Mono.error(new RuntimeException("HTTP error: " + response.statusCode())))
        .bodyToMono(byte[].class)
        .subscribeOn(scheduler)
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)))
        .subscribe(
            bytes -> saveImageToFile(bytes, destinationPath),
            error -> logger.error(error.getMessage()));
  }

  private void saveImageToFile(byte[] imageBytes, String destinationPath) {
    File file = new File(destinationPath);
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(imageBytes);
    } catch (IOException e) {
      throw new ScraperException(e);
    }
  }
}
