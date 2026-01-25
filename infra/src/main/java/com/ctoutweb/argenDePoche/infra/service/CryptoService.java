package com.ctoutweb.argenDePoche.infra.service;

public interface CryptoService {

  /**
   * Renvoie le hash d'une chaine de charactères
   *
   * @param textToHash Le text a hasher
   *
   * @return Le hash du text
   */
  public String hashText(String textToHash);
}
