package com.ctoutweb.argenDePoche.infra.model.dto;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

/**
 * Model regroupant l'image a streamer de l'enfant et du mediatype de l'image
 *
 * @param imageMediaType
 * @param childImageDate
 */
public record ImageStreaming(
        MediaType imageMediaType,
        Flux<DataBuffer> childImageDate
) {
}
