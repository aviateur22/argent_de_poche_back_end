package com.ctoutweb.argenDePoche.infra.util;

import java.util.Random;

public class NumberUtil {
    private NumberUtil() {
        throw new IllegalStateException("Classe utilitaire");
    }

    public static Long generateRandomLongNumberBetweenMinAndMax(long min, long max) {

        Random rand = new Random();
        return rand.nextLong((max - min) + 1) + min;
    }
}
