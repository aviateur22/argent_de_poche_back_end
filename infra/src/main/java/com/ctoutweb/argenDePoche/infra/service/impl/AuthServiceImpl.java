package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.exception.EmailExistException;
import com.ctoutweb.argenDePoche.infra.repository.ParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import com.ctoutweb.argenDePoche.infra.service.AuthService;
import com.ctoutweb.argenDePoche.infra.service.CryptoService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

  private final ParentRepository parentRepository;
  private final CryptoService cryptoService;

  public AuthServiceImpl(ParentRepository parentRepository, CryptoService cryptoService) {
    this.parentRepository = parentRepository;
    this.cryptoService = cryptoService;
  }


  @Override
  public Mono<Long> registerParent(String email, String parentNickName, String password) {
    var parentToRegister = new ParentEntity();

    parentToRegister.setNickname(parentNickName);
    parentToRegister.setEmail(email);
    parentToRegister.setPassword(cryptoService.hashText(password));

    return parentRepository.findByEmail(email)
            .hasElement()
            .flatMap(parentExist -> {
              if(parentExist)
                return Mono.error(new EmailExistException("Cet email existe déja"));

              return parentRepository.save(parentToRegister)
                      .map(ParentEntity::getId);
            });
  }
}
