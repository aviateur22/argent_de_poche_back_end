package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.movementReason;

import java.util.List;

/**
 * Instance regroupant les informations sur un mouvement d'argent
 * Cette instance sera utilisée par le client afin de renvoyer les données nécessaire pour
 * l'ajout d'un mouvement d'argent
 */
public record AvailableReasonMovement(
        String actionName,
        String reasonCode,
        String addActionCode,
        String removeActionCode) {

  /**
   * Factory permettant de renvoyer une nouvelle Liste de AccountAction à la creation d'un compte
   */
  public static List<AvailableReasonMovement> create() {
    var createAccountAction = new AvailableReasonMovement("", "", "", "");
    return List.of(createAccountAction);
  }
}
