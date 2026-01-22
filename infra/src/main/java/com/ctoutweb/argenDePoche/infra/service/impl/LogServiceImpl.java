package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.service.LogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void errorLog(String message) {
        LOGGER.error(message);
    }

    @Override
    public void infoLog(String message) {
        LOGGER.info(message);
    }
}
