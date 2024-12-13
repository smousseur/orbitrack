package com.smousseur.orbitrack;

import com.smousseur.orbitrack.scraper.service.ScraperService;
import java.time.Duration;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@SpringBootApplication
public class OrbitrackScraperApplication {
  private static final Logger logger = LoggerFactory.getLogger(OrbitrackScraperApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(OrbitrackScraperApplication.class, args);
  }

  @Bean
  public CommandLineRunner run(ScraperService scraperService) {
    return args -> {
      LocalDateTime now = LocalDateTime.now();
      scraperService.process();
      Duration elaspedTime = Duration.between(now, LocalDateTime.now());
      logger.info(
          "Elapsed time {} minutes and {} seconds",
          elaspedTime.toMinutesPart(),
          elaspedTime.toSecondsPart());
    };
  }

  @Bean
  public WebClient webClient() {
    ConnectionProvider connectionProvider =
        ConnectionProvider.builder("custom")
            .maxConnections(500)
            .pendingAcquireMaxCount(5000)
            .maxIdleTime(Duration.ofSeconds(30))
            .build();
    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create(connectionProvider)))
        .build();
  }
}
