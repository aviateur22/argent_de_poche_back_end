package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.config.authentication.LoginInformation;
import com.ctoutweb.argenDePoche.infra.config.authentication.LoginStatus;
import com.ctoutweb.argenDePoche.infra.repository.DelayLoginRepository;
import com.ctoutweb.argenDePoche.infra.repository.LoginRepository;
import com.ctoutweb.argenDePoche.infra.repository.entity.DelayLoginEntity;
import com.ctoutweb.argenDePoche.infra.repository.entity.LoginEntity;
import com.ctoutweb.argenDePoche.infra.service.CryptoService;
import com.ctoutweb.argenDePoche.infra.service.LoginManagerService;
import com.ctoutweb.argenDePoche.infra.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static com.ctoutweb.argenDePoche.infra.constant.LoginConstant.*;

@Service
public class LoginManagerServiceImpl implements LoginManagerService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final CryptoService cryptoService;
    private final LoginRepository loginRepository;
    private final DelayLoginRepository delayLoginRepository;
    private final LoginInformation loginInformation;

    public LoginManagerServiceImpl(
            CryptoService cryptoService,
            LoginRepository loginRepository,
            DelayLoginRepository disableLoginRepository,
            LoginInformation disableLoginInformation) {
        this.cryptoService = cryptoService;
        this.loginRepository = loginRepository;
        this.delayLoginRepository = disableLoginRepository;
        this.loginInformation = disableLoginInformation;
    }

    @Override
    public Mono<LoginStatus> isUserLoginAuthorized(long userId) {
        final LocalDateTime actualLoginTime = LocalDateTime.now();

        return delayLoginRepository.findFirstByParentId(userId)
                .flatMap(delayLogin -> {
                    LocalDateTime loginUnavailableUntil = delayLogin.getDelayLoginUntil();

                    if(loginInformation.isLoginEnable(actualLoginTime, loginUnavailableUntil)) {
                        return delayLoginRepository.delete(delayLogin)
                                .thenReturn(new LoginStatus(true, null));
                    }

                    return Mono.just(new LoginStatus(
                            false,
                            String.format("Vous pourrez vous reconnecter à partir du %s", DateUtil.toDateHour(loginUnavailableUntil))
                    ));
                })
                .switchIfEmpty(Mono.just(new LoginStatus(true, null)));
    }

    @Override
    public Mono<LoginEntity> updateUserLoginInformation(long loginParentId, boolean isAuthenticationValid) {
        final LocalDateTime loginTime = LocalDateTime.now();
        final  boolean HAS_TO_BE_ChECK = true;

        LoginEntity loginInformation = new LoginEntity(loginParentId,isAuthenticationValid, HAS_TO_BE_ChECK, loginTime);
        return loginRepository.save(loginInformation);
    }

    @Override
    public Mono<LoginStatus> manageUserLogin(long loginParentId, String plainTextPasswordSendByUser, String hashPassword) {
        boolean isAuthenticationValid = cryptoService.isHashValid(plainTextPasswordSendByUser,hashPassword);

        return updateUserLoginInformation(loginParentId, isAuthenticationValid)
                .flatMap(data -> {
                    if(isAuthenticationValid)
                        return Mono.just(new LoginStatus(true, null));

                    return manageFailedLogin(loginParentId)
                            .map(failedLoginMessage -> new LoginStatus(false, failedLoginMessage));
                });
    }


    public Mono<String> manageFailedLogin(long parentId) {
        // List des dernieres connexions du client
        return loginRepository
                .findByParentIdOrderByLoginAtDesc(parentId)
                .take(USER_LOGIN_COUNT)
                .collectList()
                .flatMap(lastUserLoginList -> {
                    long remainingLoginAttempt = remainingLoginAttempts(lastUserLoginList);

                    if(remainingLoginAttempt > 0)
                        return Mono.just(String.format("Login ou mot de passe invalide. Il vous reste %s tentative de connexion", remainingLoginAttempt));

                    return resetUserConnexionStatus(lastUserLoginList)
                            .then(disableLoginForPeriod(parentId));
                });
    }

    private long remainingLoginAttempts(List<LoginEntity> lastUserLoginList) {
        // Récupérartion du nombre de connexion invalide
        long loginAttemptErrorCount = lastUserLoginList
                .stream()
                .filter(login-> !login.getIsLoginSuccess() && login.getHasToBeCheck())
                .count();

        // Calcul du nombre de connexion restante en cas d'erreur
        return FAILED_LOGIN_ATTEMPT_AVAILABLE - loginAttemptErrorCount;
    }


    /**
     * Désecative la connexion utilisateur et renvoie un message associé a ce nouvel etat
     *
     * @param loginParentId L'identifiant de l'utilisateur qui se connecte
     *
     * @return Le message associé a la désactivation
     */
    public Mono<String> disableLoginForPeriod(long loginParentId) {
        // Calcul heure de déblocage de connexion
        LocalDateTime loginDisableUntil = LocalDateTime.now()
                .plusMinutes(LOGIN_DELAY_IN_MINUTE);

        DelayLoginEntity delayLoginToSave = new DelayLoginEntity(loginParentId, loginDisableUntil);

        return delayLoginRepository
            .findFirstByParentId(loginParentId)
                .flatMap(login ->
                    delayLoginRepository.delete(login)
                            .then(delayLoginRepository.save(delayLoginToSave))
                )
            .switchIfEmpty(delayLoginRepository.save(delayLoginToSave))
            .thenReturn(String.format("Login ou mot de passe invalide. Vous pourrez essayer de vous reconnecter à partir du %s", DateUtil.toDateHour(loginDisableUntil)));
    }

    /**
     * Mise a jour des status de control des login d'un user
     *
     * @param lastUserLoginList Liste des dernieres connexions d'un utilsateur a mettre à jour
     */
    public Mono<Void> resetUserConnexionStatus(List<LoginEntity> lastUserLoginList) {

        lastUserLoginList.forEach(userLogin -> userLogin.setHasToBeCheck(false));
        return loginRepository.saveAll(lastUserLoginList).then();
    }
}
