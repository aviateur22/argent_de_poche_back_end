package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.exception.AuthenticationException;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {
  /**
   * Connexion d'un parent
   *
   * @param loginDto Les données de connexion avec email + mot de passe
   *
   * @return Si connexion authorisée renvoie un JWT ,utilisateur id et roles
   *
   * @throws AuthenticationException renvoyé si connexion est bloqué temporairement ou si mot de passe invalide
   */
  Mono<LoginResponseDto> login(LoginDto loginDto) throws AuthenticationException;

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
