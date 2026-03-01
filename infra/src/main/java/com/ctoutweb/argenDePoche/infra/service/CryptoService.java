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

  /**
   * Vérification d'un hash et text
   *
   * @param hash String
   * @param plainText String
   *
   * @return True si la comparaison du hash et du text en claire est valide
   */
  public boolean isHashValid(String plainText, String hash);
}
