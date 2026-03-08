package com.ctoutweb.argenDePoche.infra.config.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal  implements UserDetails, UserLoginProcess {
        private long id;
        private String parentName;
        private String familyName;
        private String email;
        private String hashPassword;
        private String plainTextPassword;
        private Collection< ? extends GrantedAuthority > authorities;
        private Boolean isAccountActive;

    public UserPrincipal(
            long id,
            String email,
            String parentName,
            String familyName,
            String plainTextPassword,
            String hashPassword,
            List<String> userRoleNames ,
            Boolean isAccountActive) {
        this.id = id;
        this.email = email;
        this.parentName = parentName;
        this.familyName = familyName;
        this.plainTextPassword = plainTextPassword;
        this.hashPassword = hashPassword;
        this.authorities =  convertRoleUserToAuthorities(userRoleNames);
        this.isAccountActive = isAccountActive;
    }

    public UserPrincipal(
            long id,
            String email,
            List<SimpleGrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return plainTextPassword;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getHashPassword() {
        return hashPassword;
    }

    @Override
    public String getParentName() {
        return parentName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
