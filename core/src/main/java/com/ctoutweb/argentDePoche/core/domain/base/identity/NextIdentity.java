package com.ctoutweb.argentDePoche.core.domain.base.identity;

/**
 * Contrat a implementer pour la génération de nouveau
 */
public interface NextIdentity<T> {
    Ident<T> generateNextIdentity();
}
