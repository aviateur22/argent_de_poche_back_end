package com.ctoutweb.argentDePoche.application.spi;

/**
 * Contrats définissant tous les types de génération aléatoire
 */
public interface RandomProvider {

    /**
     * Génération d'un text aléatoire
     *
     * @return Le texte aléatoire
     */
    String generateUniqueRandomUuid();
}
