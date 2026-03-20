package com.ctoutweb.argentDePoche.application.query.dto;

import reactor.core.publisher.Mono;

public record QrCodeDto(byte[] qrCodeBytes) {
}
