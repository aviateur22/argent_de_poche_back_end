package com.ctoutweb.argenDePoche.infra.config.authentication;

import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapToUserPrincipal {
    /**
     * Map un SellerEntity en UserPrincipal
     *
     * @param parentIdentity ParentIdentity - Le parent qui est connecté
     * @return UserPrincipal
     */
    public UserPrincipal map(ParentEntity parentIdentity, String familyName, List<String> parentRoles) {
        return new UserPrincipal(
                parentIdentity.getId(),
                parentIdentity.getEmail(),
                parentIdentity.getName(),
                familyName,
                parentIdentity.getPassword(),
                convertRoleUserToAuthorities(parentRoles),
                parentIdentity.getIsAccountActive()
        );
    }

    /**
     * convertion list<RoleSellerEntity> en List<GrantedAuthority>
     *
     * @param roleNames Les nom de roles disponible de la personne qui se connecte
     *
     * @return La liste des roles de la personne qui se connecte mappé en List<SimpleGrantedAuthority>
     */
    public List<SimpleGrantedAuthority> convertRoleUserToAuthorities(List<String> roleNames) {
        return roleNames
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
