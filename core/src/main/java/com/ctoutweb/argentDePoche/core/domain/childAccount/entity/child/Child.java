package com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child;

import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImage;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildException;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildNameException;

import java.util.Objects;

public record Child(ChildIdentity childIdentity, String firstName, ChildImage childImage) {

    static final int  FIRST_NAME_MIN_LENGTH = 3;

    public Child {
        Objects.requireNonNull(firstName, "Le nom de l'enfant est obligatoire: " + this);

        if(firstName.length() < FIRST_NAME_MIN_LENGTH)
            throw new ChildNameException("Le prénom ne peux pas être inférieur a 3 charactères");
    }

    /**
     * Renvoie une nouvelle instance avec un nouveau prenom
     *
     * @param updatedName Le nouveau prenom de l'enfant
     *
     * @return L'insatnce de Child mis à jour
     */
    public Child with(String updatedName) {
        return new Child(this.childIdentity, updatedName, this.childImage);
    }

    /**
     * Rencoie une nouvelle instance avec l'image de l'enfant modifiée
     *
     * @param updatedChildImage La nouvelle instance de l'image de l'enfant
     *
     * @return L'insatnce de Child mis à jour
     */
    public Child with(ChildImage updatedChildImage) {
        return new Child(this.childIdentity, this.firstName, updatedChildImage);
    }

    public Child updateChildName(String updatedChildName) {
        if(updatedChildName == null)
            throw new ChildException("Le prénom ne peux pas être inférieur a 3 charactères");

        if(updatedChildName.equalsIgnoreCase(this.firstName))
            throw new ChildException("Le prénom de l'enfant doit être différent");

        if(updatedChildName.length() < FIRST_NAME_MIN_LENGTH)
            throw new ChildException("Le prénom ne peux pas être inférieur a 3 charactères");

       return  with(updatedChildName);
    }

    public Child updateImage(String newImageRandomName, ImageExtension imageExtension) {
        ChildImage updatedChildImage = this.childImage.updateChildImage(newImageRandomName, imageExtension);
        return with(updatedChildImage);
    }
}
