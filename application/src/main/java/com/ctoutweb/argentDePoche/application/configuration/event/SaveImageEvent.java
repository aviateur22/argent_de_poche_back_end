package com.ctoutweb.argentDePoche.application.configuration.event;

import java.io.InputStream;

public record SaveImageEvent(String imageName, byte[] updatedImageBytes) {
}
