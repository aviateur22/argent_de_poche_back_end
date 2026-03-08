package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.config.authentication.UserPrincipal;
import com.ctoutweb.argenDePoche.infra.exception.AuthenticationException;
import com.ctoutweb.argenDePoche.infra.exception.EmailExistException;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginResponseDto;
import com.ctoutweb.argenDePoche.infra.model.jwt.JwtGenerated;
import com.ctoutweb.argenDePoche.infra.repository.ParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.RoleParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import com.ctoutweb.argenDePoche.infra.repository.entity.RoleParentEntity;
import com.ctoutweb.argenDePoche.infra.service.AuthService;
import com.ctoutweb.argenDePoche.infra.service.CryptoService;
import com.ctoutweb.argenDePoche.infra.service.JwtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {
  private static final Logger LOGGER = LogManager.getLogger();
  private final ParentRepository parentRepository;
  private final CryptoService cryptoService;
  private final ReactiveAuthenticationManager reactiveAuthenticationManager;
  private final RoleParentRepository roleParentRepository;
  private final JwtService jwtService;
  private final TransactionalOperator txOperator;

  public AuthServiceImpl(
          ParentRepository parentRepository,
          CryptoService cryptoService,
          ReactiveAuthenticationManager reactiveAuthenticationManager,
          RoleParentRepository roleParentRepository,
          JwtService jwtService, TransactionalOperator txOperator) {
    this.parentRepository = parentRepository;
    this.cryptoService = cryptoService;
    this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    this.roleParentRepository = roleParentRepository;
    this.jwtService = jwtService;
    this.txOperator = txOperator;
  }


  @Override
  public Mono<Long> registerParent(String email, String parentNickName, String password) {

    // Par default le compte est actif
    final var isParentActif = true;

    var parentToRegister = new ParentEntity();
    parentToRegister.setName(parentNickName);
    parentToRegister.setEmail(email);
    parentToRegister.setPassword(cryptoService.hashText(password));
    parentToRegister.setIsAccountActive(isParentActif);

    return parentRepository.findByEmail(email)
            .hasElement()
            .flatMap(parentExist -> {
              if(parentExist)
                return Mono.error(new EmailExistException("Cet email existe déja"));

              return parentRepository.save(parentToRegister)
                      .flatMap(parent -> {
                        // ID du role parent
                        final var parentRoleId = 1;

                        var roleParentToRegister =  new RoleParentEntity();
                        roleParentToRegister.setParentId(parent.getId());
                        roleParentToRegister.setRoleId(parentRoleId);

                        return roleParentRepository.save(roleParentToRegister)
                                .thenReturn(parent.getId());
                      });
            });
  }

  @Override
  public Mono<LoginResponseDto> login(LoginDto loginDto) throws AuthenticationException {
      UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

      return reactiveAuthenticationManager.authenticate(user)
              .flatMap(authentication  -> {
                UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

                JwtGenerated jwt = jwtService.generate(userPrincipal);
                return jwtService.saveJwt(userPrincipal.getId(), jwt, loginDto.email())
                        .thenReturn(
                                new LoginResponseDto(
                                        jwt.getJwtToken(),
                                        userPrincipal.getId(),
                                        userPrincipal.getParentName(),
                                        userPrincipal.getFamilyName(),
                                        userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                                        jwt.getExpiredAt(),
                                        String.format("Bonjour %s", userPrincipal.getParentName()))
                        );
              })
              .doOnError(e -> LOGGER.error("Login failed", e));
  }
}
