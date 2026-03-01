package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.service.CryptoService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CryptoServiceImpl implements CryptoService {
  private final PasswordEncoder passwordEncoder;

  public CryptoServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String hashText(String textToHash) {
    return passwordEncoder.encode(textToHash);
  }

  @Override
  public boolean isHashValid(String plainText, String hash) {
    return passwordEncoder.matches(plainText, hash);
  }
}
