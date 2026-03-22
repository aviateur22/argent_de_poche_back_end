package com.ctoutweb.argenDePoche.infra.config.authorizationFilter;

import java.util.Arrays;

public enum AuthorizedUri {
  AUTH("/auth");

  private AuthorizedUri(String path) {
    this.path = path;
  }
  private String path;

  /**
   * Recherche si un requested path est en accés public
   * ou soumis a authorisation
   *
   * @param requestedPath L'url a vérifier
   *
   * @return True si l'url est libre d'accés
   */
  public static boolean isPathAuthorized(String requestedPath) {
    return Arrays
            .stream(AuthorizedUri.values())
            .anyMatch(authorizedPath -> requestedPath.contains(authorizedPath.path));
  }
}
