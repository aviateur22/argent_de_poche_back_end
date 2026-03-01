package com.ctoutweb.argenDePoche.infra.config.authentication;

import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapToUserPrincipal {
    /**
     * Map un SellerEntity en UserPrincipal
     *
     * @param parentIdentity ParentIdentity - Le parent qui est connecté
     * @return UserPrincipal
     */
    public UserPrincipal map(ParentEntity parentIdentity, List<String> parentRoles, String plainTextPassword) {
        return new UserPrincipal(
                parentIdentity.getId(),
                parentIdentity.getEmail(),
                parentIdentity.getNickname(),
                plainTextPassword,
                parentIdentity.getPassword(),
                parentRoles,
                parentIdentity.getIsAccountActive()
        );
    }
}
