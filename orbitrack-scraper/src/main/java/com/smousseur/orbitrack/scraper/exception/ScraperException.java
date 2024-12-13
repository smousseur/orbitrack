package com.smousseur.orbitrack.scraper.exception;

public class ScraperException extends RuntimeException {
  public ScraperException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScraperException(Throwable cause) {
    super(cause);
  }
}
