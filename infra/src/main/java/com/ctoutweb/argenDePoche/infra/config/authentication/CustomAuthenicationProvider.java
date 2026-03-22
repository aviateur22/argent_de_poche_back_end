package com.ctoutweb.argenDePoche.infra.config.authentication;

import com.ctoutweb.argenDePoche.infra.exception.AuthenticationException;
import com.ctoutweb.argenDePoche.infra.repository.FamilyRepository;
import com.ctoutweb.argenDePoche.infra.repository.ParentFamilyAccountRepository;
import com.ctoutweb.argenDePoche.infra.repository.ParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.RoleParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.dto.ParentRoleProjection;
import com.ctoutweb.argenDePoche.infra.service.LoginManagerService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenicationProvider implements ReactiveAuthenticationManager {
    private final ParentRepository parentRepository;
    private final RoleParentRepository roleParentRepository;
    private final MapToUserPrincipal mapToUserPrincipal;
    private final ParentFamilyAccountRepository parentFamilyAccountRepository;
    private final LoginManagerService loginManagerService;
    private final FamilyRepository familyRepository;

    public CustomAuthenicationProvider(
            ParentRepository parentRepository,
            RoleParentRepository roleParentRepository,
            MapToUserPrincipal mapToUserPrincipal,
            ParentFamilyAccountRepository parentFamilyAccountRepository,
            LoginManagerService loginManagerService, FamilyRepository familyRepository) {
        this.parentRepository = parentRepository;
      this.roleParentRepository = roleParentRepository;
      this.mapToUserPrincipal = mapToUserPrincipal;
      this.parentFamilyAccountRepository = parentFamilyAccountRepository;
      this.loginManagerService = loginManagerService;
      this.familyRepository = familyRepository;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String plainTextPasswordSendByUser = authentication.getCredentials().toString();
        String emailSendByUser = authentication.getName();

        return findParent(emailSendByUser, plainTextPasswordSendByUser)
                .switchIfEmpty(Mono.error(new AuthenticationException("Cet email n'existe pas")))
                .flatMap(userPrincipal -> {
                    long loginParentId = userPrincipal.getId();
                    String hashPassword = userPrincipal.getHashPassword();

                    return loginManagerService.isUserLoginAuthorized(loginParentId)
                            .flatMap(loginStatus -> {
                                if(!loginStatus.isLoginAuthorized())
                                    throw new AuthenticationException(loginStatus.loginErrorMessage());

                                return loginManagerService.manageUserLogin(loginParentId, plainTextPasswordSendByUser, hashPassword)
                                        .flatMap(passwordStatus -> {
                                              if(!passwordStatus.isLoginAuthorized())
                                                throw new AuthenticationException(passwordStatus.loginErrorMessage());


                                          return Mono.just(new UsernamePasswordAuthenticationToken(userPrincipal, authentication.getCredentials()));
                                        });
                            });
                });
    }

    private Mono<UserPrincipal> findParent(String email, String plainTextPassword) {
        return parentRepository
                .findByEmail(email)
                .flatMap(parent -> {
                      return parentFamilyAccountRepository.findAllByParentId(parent.getId())
                              .collectList()
                              .flatMap(families -> {
                                  return familyRepository.findByFamilyAccountId(families.get(0).getFamilyAccountId())
                                          .flatMap(family -> {
                                            return roleParentRepository.getParentRoles(parent.getId())
                                                    .collectList()
                                                    .map(parentRoles -> {
                                                      var roles = parentRoles
                                                              .stream()
                                                              .map(ParentRoleProjection::roleName)
                                                              .toList();
                                                      return mapToUserPrincipal.map(parent, family.getName(), roles);
                                                    });
                                          });
                              });

                });
    }
}
