package com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage;

import com.ctoutweb.argentDePoche.core.domain.exception.ChildImageException;

import java.util.Objects;

public record ChildImage(String imageRandomName, String folderPath) {

    public ChildImage {
        Objects.requireNonNull(imageRandomName, "Le nom de l'image ne peut pas être vide");
        Objects.requireNonNull(folderPath, "Le path de sauvgarde de l'image est manquant");
    }

    /**
     * Methode factory permetant de renvoyer une nouvelle instance de ChildImage
     *
     * @param imageRandomName Le nom de l'image généré de manière aléatoire
     * @param folderPath Le folder de sauvegarde
     *
     * @return ChildImage
     */
    public static ChildImage create(String imageRandomName, String folderPath) {
        return new ChildImage(imageRandomName, folderPath);
    }

    private ChildImage withUpdatedImageName(String updatedImageRandomName) {
        return new ChildImage(updatedImageRandomName, this.folderPath);
    }

    public ChildImage updateChildImage(String newImageRandomName) {
        if(newImageRandomName == null || newImageRandomName.isEmpty())
            throw new ChildImageException("Le nom de la nouvelle image ne peut pas être nulle");

        return withUpdatedImageName(newImageRandomName);
    }
}
