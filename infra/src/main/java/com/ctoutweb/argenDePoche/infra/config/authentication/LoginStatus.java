package com.ctoutweb.argenDePoche.infra.config.authentication;

public record LoginStatus(boolean isLoginAuthorized, String loginErrorMessage) {
}
