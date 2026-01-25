package com.ctoutweb.argenDePoche.infra.service;

import reactor.core.publisher.Mono;

public interface AuthService {

  /**
   * Enregistrement d'un parent créant un nouveau compte familale
   *
   * @param email L'email du parent
   * @param parentNickName Le nom du parent
   * @param password Le mot de passe
   *
   * @return L'identifiant technique de l'enregsitrement du parent
   */
  Mono<Long> registerParent(String email,String parentNickName, String password);
}
