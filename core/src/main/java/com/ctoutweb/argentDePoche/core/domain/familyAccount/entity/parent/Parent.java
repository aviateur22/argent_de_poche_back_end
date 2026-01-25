package com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent;

import java.util.Objects;

public record Parent(ParentIdentity parentIdentity, String name) {

  public Parent {
    Objects.requireNonNull(parentIdentity);
    Objects.requireNonNull(name);
  }

  /**
   * Creation d'un nouveau parent
   *
   * @param parentIdentity Lidentity du parent
   * @param name Le nom du parent
   *
   * @return La famille créée
   */
  public static Parent create(ParentIdentity parentIdentity, String name) {
    return new Parent(parentIdentity, name);
  }

}
