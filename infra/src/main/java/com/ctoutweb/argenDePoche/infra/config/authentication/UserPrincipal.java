package com.ctoutweb.argenDePoche.infra.config.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(
        long id,
        String email,
        String parentName,
        String familyName,
        String hashPassword,
        Collection<? extends GrantedAuthority> authorities,
        Boolean isAccountActive
) implements UserDetails, UserLoginProcess {
  public static UserPrincipal initialize(long id, String email, List<SimpleGrantedAuthority> authorities) {
    return new UserPrincipal(
            id,
            email,
            null,
            null,
            null,
            authorities,
            true
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return hashPassword;
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
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
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

  public String getFamilyName() {
        return familyName;
  }
}
