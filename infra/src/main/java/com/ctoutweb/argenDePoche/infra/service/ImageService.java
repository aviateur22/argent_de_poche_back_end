package com.ctoutweb.argenDePoche.infra.service;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Gestion des image
 */
public interface ImageService {

    Flux<DataBuffer> streamImage(String childImageNameWithExtension);

    /**
     * Sauvgarde d'une image de le l'enfant
     *
     * @param childImageFile - L'image de l'enfant
     * @param childImageRandomName - Le nom unique de l'image qui est généré pour sauvegarde
     *
     * @return Le nom unique de l'image qui est sauvegardé
     *
     */
    Mono<String> saveImage(FilePart childImageFile, String childImageRandomName);

    /**
     * Une Image a supprimer
     *
     * @param imageNameToDelete Nom de l'image a supprimer
     */
    Mono<Void> deleteImage(String imageNameToDelete);
}
