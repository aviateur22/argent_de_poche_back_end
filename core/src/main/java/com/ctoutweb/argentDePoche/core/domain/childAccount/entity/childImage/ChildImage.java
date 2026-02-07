package com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage;

import com.ctoutweb.argentDePoche.core.domain.exception.ChildImageException;

import java.util.Objects;

public record ChildImage(
        String imageRandomName,
        ImageExtension imageExtension
) {

    public ChildImage {
        Objects.requireNonNull(imageExtension, "L'extension de l'image est obligatoire");
        Objects.requireNonNull(imageRandomName, "Le nom de l'image ne peut pas être vide");
    }

    private ChildImage with(String updatedImageRandomName, ImageExtension imageExtension) {
        return new ChildImage(updatedImageRandomName, imageExtension);
    }

    /**
     * Methode factory permetant de renvoyer une nouvelle instance de ChildImage
     *
     * @param imageRandomName Le nom de l'image généré de manière aléatoire
     *
     * @return ChildImage
     */
    public static ChildImage create(String imageRandomName, ImageExtension imageExtension) {
        return new ChildImage(imageRandomName, imageExtension);
    }

    /**
     * Mise à jour de l'image d'un enfont
     *
     * @param newImageRandomName Le nouveau nom de l'image généré de maniére Random     *
     * @param extension Le type d'image
     *
     * @return La nouvelle image mis a jour
     */
    public ChildImage updateChildImage(String newImageRandomName, ImageExtension extension) {
        if(newImageRandomName == null || newImageRandomName.isEmpty())
            throw new ChildImageException("Le nom de la nouvelle image ne peut pas être nulle");

        return with(newImageRandomName, extension);
    }
}
