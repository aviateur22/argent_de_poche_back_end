package com.ctoutweb.argenDePoche.infra.config.authentication;

/**
 * Contrat permattant de transferer les données d'un utilisateur en cours de connexion
 */
public interface UserLoginProcess {

    /**
     * L'identification de la personne en cours de login
     *
     * @return L'identification
     */
    long getId();

    /**
     * Renvoie le mot de passe HAshé d'un utilisateur
     *
     * @return Le mot de passe hashé
     */
    String getHashPassword();

    /**
     * Nicname de la personne
     *
     * @return Le nickname
     */
    String getParentName();
}
