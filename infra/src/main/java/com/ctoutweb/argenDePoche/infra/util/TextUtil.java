package com.ctoutweb.argenDePoche.infra.util;

import java.util.UUID;

public class TextUtil {

    /**
     * Génération d'un String Aléatoire de type UUID
     *
     * @return String
     */
    public static String getRandomNameUUID() {
        byte[] timeNow = ("heure actuelle" +" " + System.currentTimeMillis()).getBytes();
        return UUID.nameUUIDFromBytes(timeNow).toString();
    }
}
