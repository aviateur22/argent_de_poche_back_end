package com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family;

import com.ctoutweb.argentDePoche.core.domain.exception.FamilyNameException;

import java.util.Objects;

public record Family(FamilyIdentity familyIdentity, String familyName) {
    public Family {
            Objects.requireNonNull(familyIdentity, "L'identifiant de la famille est obligatoire: " + this);
            Objects.requireNonNull(familyName, "Le nom de famille ne peut pas être vide: " + this);

            if(familyName.isEmpty())
                throw new FamilyNameException("Le nom de famille ne peut pas être vide");
    }

    /**
     * Creation d'une nouvelle famille
     *
     * @param familyIdentity Lidentity de la famille
     * @param familyName Le nom de la famille
     *
     * @return La famille créée
     */
    public static Family create(FamilyIdentity familyIdentity, String familyName) {
        return new Family(familyIdentity, familyName);
    }

    private Family withFamilyName(String updatedFamilyName) {
        return new Family(familyIdentity, updatedFamilyName);
    }
    public Family updateFamilyName(String updatedFamilyName) {
        if(updatedFamilyName == null || updatedFamilyName.isEmpty())
            throw new FamilyNameException("Le nom de famille ne peut pas être vide");

        if(updatedFamilyName.equalsIgnoreCase(familyName))
            throw new FamilyNameException("Choisir un nouveau nom de famille");

        return withFamilyName(updatedFamilyName);
    }
}
