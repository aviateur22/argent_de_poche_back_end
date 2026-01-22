package com.ctoutweb.argenDePoche.infra.service;

import java.io.InputStream;

/**
 * Gestion des image
 */
public interface ImageService {

    /**
     * Sauvgarde d'une image
     *
     * @param imageNameToSave - Nom de l'image
     * @param imageBytes - L'image a sauvegarder
     *
     */
    void saveImage(String imageNameToSave, byte[] imageBytes);

    /**
     * Image a supprimer
     *
     * @param imageNameToDelete Nom de l'image a supprimer
     */
    void deleteImage(String imageNameToDelete);
}
