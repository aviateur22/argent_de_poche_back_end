package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.config.authentication.LoginStatus;
import com.ctoutweb.argenDePoche.infra.repository.entity.LoginEntity;
import reactor.core.publisher.Mono;

public interface LoginManagerService {

    /**
     * Vérification qu'un utilisateur puisse se connecter
     *
     * @param userId L'identifiant de l'utilisateur que se connecte
     *
     * @return True si l'utilisateur peut se connecter
     */
    Mono<LoginStatus> isUserLoginAuthorized(long userId);

    /**
     * Gestion de la connexion d'un utilisateur qui est en cours de connexion
     *
     * @param userId L'identifiant de l'utilisateur que se connecte
     * @param plainTextPasswordSendByUser Le mot de passe en claire fournis par l'utilsateur lors de la locannexion
     * @param hashPassword Le mot de passe hashé récupéré en base
     */
    Mono<LoginStatus> manageUserLogin(long userId, String plainTextPasswordSendByUser, String hashPassword);

    /**
     * Mise a jour des informations de connexion d'un utilisateur.
     * Cette methode est appelé uniquement si l'utilisateur n'est pas restreint à la connexion
     *
     * @param userId L'identifiant de l'utilisateur
     *
     * @param isAuthenticationValid Validité de l'authentification de l'utilisateur qui se connecte
     */
    Mono<LoginEntity> updateUserLoginInformation(long userId, boolean isAuthenticationValid);
}
