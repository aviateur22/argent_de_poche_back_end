package com.ctoutweb.argentDePoche.application.configuration.event;

public record SaveImageEvent(String imageName, byte[] updatedImageBytes) {
}
