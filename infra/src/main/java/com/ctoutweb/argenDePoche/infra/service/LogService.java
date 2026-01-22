package com.ctoutweb.argenDePoche.infra.service;

public interface LogService {
    void errorLog(String message);
    void infoLog(String message);
}
