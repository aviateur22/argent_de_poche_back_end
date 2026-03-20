package com.ctoutweb.argentDePoche.application.spi;

import reactor.core.publisher.Mono;

/**
 * Provider de QR code
 */
public interface QrCodeProvider {

  /**
   * Generation d'une image QR code contenant un URL a acceder
   *
   * @param url L'url a inclure dans le QR code
   * @param imageHeightAndWidth La taille de l'image du QR code
   *
   * @return
   */
  Mono<byte[]> generate(String url, int imageHeightAndWidth);
}
