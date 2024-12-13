package com.smousseur.orbitrack;

import com.smousseur.orbitrack.referential.service.SpaceTrackService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class OrbitrackReferentialApplication {
  private static final String URL = "https://www.space-track.org";

  public static void main(String[] args) {
    SpringApplication.run(OrbitrackReferentialApplication.class, args);
  }

  @Bean
  public CommandLineRunner run(SpaceTrackService trackService) {
    return args -> trackService.process();
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder().baseUrl(URL).build();
  }
}
