package com.ctoutweb.argenDePoche.infra.service;

import reactor.core.publisher.Mono;

public interface AdminService {
  /**
   * Génération des données nécéssaire à la prochaine période d'aregnt de poche
   * Periode -> un compte d'argent de poche fonctionne sur une période en semaine ou en mois
   * Chaqie dimanche à 00h00 et a la fin d'une période:
   * -> création de la période de calendrier suivante
   * -> Reinitilaisation de l'argent de poche restant
   *
   * @return
   */
  Mono<Boolean> initializeNextPeriod();
}
