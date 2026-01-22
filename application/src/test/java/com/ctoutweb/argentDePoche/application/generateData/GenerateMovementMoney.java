package com.ctoutweb.argentDePoche.application.generateData;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementActionType;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementReason;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Stream;

public class GenerateMovementMoney {
    private final ParentIdentity parentIdentity;
    private final static int WEEKLY_MOVEMENT = 20;
    private final static int MONTHLY_MOVEMENT = 50;

    private LocalDate weeklyStartDate;
    private LocalDate weeklyEndDate;

    public GenerateMovementMoney(ParentIdentity parentIdentity) {
        this.parentIdentity = parentIdentity;
    }

    /**
     * Génération d'une liste de mouvement d'argent pour 1 mois
     *
     *
     * @param totalMonthlyPriceOfMoneyMovements
     * @param totalWeeklyPriceOfMoneyMovements
     * @return
     */
    public List<MoneyMovement> generateMonthlyMoneyMovements(BigDecimal totalMonthlyPriceOfMoneyMovements, BigDecimal totalWeeklyPriceOfMoneyMovements) {

        Assertions.assertNotNull(totalWeeklyPriceOfMoneyMovements, "Le prix du mouvement d'argent ne peut pas être NULL ");
        Assertions.assertNotNull(totalMonthlyPriceOfMoneyMovements, "Le prix du mouvement d'argent ne peut pas être NULL ");

        // Argent sur 1 semaine
        List<MoneyMovement> weeklyMoneyMovements = generateWeeklyMoneyMovements(totalWeeklyPriceOfMoneyMovements);

        // La génération de mouvement d'argent dans le mois ne dois pas prendre en compte l'aregnt de la semaine
        BigDecimal totalCorverPriceOfMoneyMovements = calculatePartialMonthMovementMoney(totalMonthlyPriceOfMoneyMovements, totalWeeklyPriceOfMoneyMovements);

        // Génération des données pour 1 mois
        List<MoneyMovement> partialMonthlyMoneyMovements = generateMonthlyMoneyMovements(totalCorverPriceOfMoneyMovements);

        // Generation d'une liste de mouvement d'argent pour le mois
        List<MoneyMovement> monthlyMoneyMovements = Stream
                        .concat(weeklyMoneyMovements.stream(), partialMonthlyMoneyMovements.stream())
                        .toList();

        // Vérification
        BigDecimal priceToControl = monthlyMoneyMovements
                .stream()
                .map(money -> {
                    BigDecimal movementPrice = money.fluctuationPrice();
                    return MovementActionType.ADD_MONEY == money.action() ?
                            movementPrice
                            :movementPrice.negate();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Controle du prix des mouvements de la balance
        Assertions.assertEquals(totalMonthlyPriceOfMoneyMovements.setScale(2, RoundingMode.UNNECESSARY), priceToControl.setScale(2, RoundingMode.UNNECESSARY));

        // Control du nombre de ligne de mouvement d'argent
        // La génération peut créer jusqu'a 2 lignes supp pour la prise en compte des centimes
        Assertions.assertTrue(monthlyMoneyMovements.size() >= WEEKLY_MOVEMENT + MONTHLY_MOVEMENT && monthlyMoneyMovements.size() <= WEEKLY_MOVEMENT + MONTHLY_MOVEMENT + 2);


        return monthlyMoneyMovements;
    }

    /**
     * Retire le cumul des mouvements de la semaine en cours au montant des mouvement du mois
     * 
     * @param monthlyMovementPrice Total des mouvement d'argent du mois
     * @param weeklyMovementPrice Tatal des mouvement d'argent de la semaine
     *                            
     * @return Le total du mois sans le montant de la semaine
     */
    private BigDecimal calculatePartialMonthMovementMoney(BigDecimal monthlyMovementPrice, BigDecimal weeklyMovementPrice) {
        BigDecimal partialMonthlyMovementMoneyPrice = null;
        if(monthlyMovementPrice.compareTo(BigDecimal.ZERO) > 0 && weeklyMovementPrice.compareTo(BigDecimal.ZERO) > 0) {
            partialMonthlyMovementMoneyPrice = monthlyMovementPrice.subtract(weeklyMovementPrice);

        } else if(monthlyMovementPrice.compareTo(BigDecimal.ZERO) < 0 && weeklyMovementPrice.compareTo(BigDecimal.ZERO) > 0) {
            partialMonthlyMovementMoneyPrice = monthlyMovementPrice.subtract(weeklyMovementPrice);

        } else if(monthlyMovementPrice.compareTo(BigDecimal.ZERO) < 0 && weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0) {
            partialMonthlyMovementMoneyPrice = monthlyMovementPrice.subtract(weeklyMovementPrice);
            
        } else if(monthlyMovementPrice.compareTo(BigDecimal.ZERO) > 0 && weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0)  {
            partialMonthlyMovementMoneyPrice = monthlyMovementPrice.subtract(weeklyMovementPrice);
        }
        
        return partialMonthlyMovementMoneyPrice;
    }


    /**
     * Generation d'une liste de mouvement d'argent pour une période de 1 semaine
     *
     * @param totalPriceOfMoneyMovements Prix de l'ensemble des mouvement d'argent souhaité
     *
     * @return La liste de mouvement d'argent ayant le prix souhaité
     */
    private List<MoneyMovement> generateWeeklyMoneyMovements(BigDecimal totalPriceOfMoneyMovements) {
        BigDecimal positiveTotalPriceOfMoneyMovements = totalPriceOfMoneyMovements.abs();
        BigDecimal unitPart =  positiveTotalPriceOfMoneyMovements.setScale(0, RoundingMode.DOWN);
        BigDecimal centimePart = positiveTotalPriceOfMoneyMovements.subtract(unitPart);

        // Les mouvements a généré sont positif si le montantTotal d'argent est positive
        boolean isMovementTypePositive = totalPriceOfMoneyMovements.compareTo(BigDecimal.ZERO) > 0;

        // Prix de 1 movement
        BigDecimal movementPrice = unitPart.divide(BigDecimal.valueOf(WEEKLY_MOVEMENT), 4, RoundingMode.UNNECESSARY);

        List<MoneyMovement> generatedMovements = generateWeeklyMoneyMovements(movementPrice, isMovementTypePositive);

        if(centimePart.compareTo(BigDecimal.ZERO) != 0)
            generatedMovements.add(generateOneMoneyMovement(centimePart, isMovementTypePositive, weeklyStartDate.atStartOfDay()));

        BigDecimal priceToControl = generatedMovements
                .stream()
                .map(MoneyMovement::fluctuationPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Assertions.assertEquals(0, positiveTotalPriceOfMoneyMovements.setScale(2, RoundingMode.UNNECESSARY).compareTo(priceToControl.setScale(2, RoundingMode.UNNECESSARY)), "Erreur dans la génération de mouvement d'argent dans la semaine");

        return generatedMovements;
    }

    /**
     * Generation d'une liste de mouvement d'argent pour une période de 1 mois
     * Le total d'argent ne prend pas en compte les mouvement d'argent de la semaaine
     *
     * @param partialMonthlyMoneyMovements Total de l'argent mensuel - Dans ce prix est déduit la somme d'argent de la semaine en cours
     *
     * @return La liste de mouvement d'argent ayant le prix souhaité
     */
    private List<MoneyMovement> generateMonthlyMoneyMovements(BigDecimal partialMonthlyMoneyMovements) {
        BigDecimal positivePartialMonthlyMoneyMovements = partialMonthlyMoneyMovements.abs();
        BigDecimal unitPart = positivePartialMonthlyMoneyMovements.setScale(0, RoundingMode.DOWN).abs();
        BigDecimal centimePart = positivePartialMonthlyMoneyMovements.subtract(unitPart).abs();

        // Les mouvements a généré sont positif si le montantTotal d'argent est positive
        boolean isMovementTypePositive = partialMonthlyMoneyMovements.compareTo(BigDecimal.ZERO) > 0;

        // Prix de 1 movement
        BigDecimal movementPrice = unitPart.divide(BigDecimal.valueOf(MONTHLY_MOVEMENT), 4, RoundingMode.UNNECESSARY);

        List<MoneyMovement> generatedMovements = generateMonthlyMoneyMovements(movementPrice, isMovementTypePositive);

        if(centimePart.compareTo(BigDecimal.ZERO) > 0) {
            LocalDateTime occuredAt = generatedMovements.get(0).occurredAt();
            generatedMovements.add(generateOneMoneyMovement(centimePart, isMovementTypePositive, occuredAt));
        }

        BigDecimal priceToControl = generatedMovements
                .stream()
                .map(MoneyMovement::fluctuationPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Control des données
        List<LocalDate> monthlyGeneratedDates = generatedMovements
                .stream()
                .map(MoneyMovement::occurredAt)
                .map(LocalDateTime::toLocalDate)
                .toList();
        Assertions.assertEquals(positivePartialMonthlyMoneyMovements.setScale(2, RoundingMode.UNNECESSARY), priceToControl.setScale(2, RoundingMode.UNNECESSARY), "Erreur dans la génération de mouvement d'argent dans le mois");
        Assertions.assertFalse(monthlyGeneratedDates.stream().allMatch(generatedDate -> generatedDate.isAfter(weeklyStartDate) && generatedDate.isBefore(weeklyEndDate)));


        return generatedMovements;
    }


    /**
     * Génération d'une liste de mouvement d'argent pour une semaine
     *
     * @param movementPrice Prix d'un mouvement
     * @param isMovementTypePositive True si le mouvement d'argent en positif
     *
     * @return La liste des mouvement d'argent sur la semaine
     */
    private List<MoneyMovement> generateWeeklyMoneyMovements(BigDecimal movementPrice, boolean isMovementTypePositive) {
        // Date pour la semaine en cours
        LocalDateTime now = LocalDateTime.now();
        weeklyStartDate = now.toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        weeklyEndDate =weeklyStartDate.plusDays(6);

        List<MoneyMovement> balances = new ArrayList<>();

        for(int i = 0; i < WEEKLY_MOVEMENT; i++){

            MovementActionType actionType = isMovementTypePositive ?
                    MovementActionType.ADD_MONEY : MovementActionType.REMOVE_MONEY;

            MoneyMovement moneyMovement = new MoneyMovement(
                    movementPrice,
                    actionType,
                    MovementReason.CHILD_BEHAVIOR,
                    randomDateTimeBetween(weeklyStartDate, weeklyEndDate),
                    parentIdentity
            );

            balances.add(moneyMovement);
        }

        return balances;
    }

    /**
     * Liste des mouvement d'argent sur une mois.
     * Cette liste filtre les doublons pouvant exister avec la liste des mouvement d'argent dela semaine
     *
     * @param movementPrice Le prix d'un mouvement d'argent
     * @param isMovementTypePositive True si le mouvement d'argent en positif
     *
     * @return La liste des mouvement d'argent sur le mois
     */
    private List<MoneyMovement> generateMonthlyMoneyMovements(
            BigDecimal movementPrice,
            boolean isMovementTypePositive) {
                // Date pour la semaine en cours
                LocalDateTime now = LocalDateTime.now();
                // Date debut et fin pour le mois en cours
                LocalDate startOfMonth = now.toLocalDate().withDayOfMonth(1);
                LocalDate endOfMonth = now.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());

                List<MoneyMovement> balances = new ArrayList<>();

                for(int i = 0; i < MONTHLY_MOVEMENT; i++){

                    MovementActionType actionType = isMovementTypePositive ?
                            MovementActionType.ADD_MONEY : MovementActionType.REMOVE_MONEY;

                    MoneyMovement moneyMovement = new MoneyMovement(
                            movementPrice,
                            actionType,
                            MovementReason.CHILD_BEHAVIOR,
                            randomDateBetweenAndNotInPeriod(startOfMonth, endOfMonth, weeklyStartDate, weeklyEndDate),
                            parentIdentity
                    );

                    balances.add(moneyMovement);
                }

        return balances;
    }

    /**
     * Renvoie 1 mouvement d'argent
     *
     * @param movementPrice Le prix de 1 mouvement
     * @param isMovementTypePositive True si le mouvement d'argent en positif
     * @return
     */
    private MoneyMovement generateOneMoneyMovement(BigDecimal movementPrice, boolean isMovementTypePositive, LocalDateTime occuredAt) {

        MovementActionType actionType = isMovementTypePositive ?
                MovementActionType.ADD_MONEY : MovementActionType.REMOVE_MONEY;

        return new MoneyMovement(
                movementPrice,
                actionType,
                MovementReason.CHILD_BEHAVIOR,
                occuredAt,
                parentIdentity
        );
    }

    /**
     * Génération de dateTime aléatoire entre 2 dates
     *
     * @param start Dates de départ
     * @param end Dates de fin
     *
     * @return Une dateTime alatoire
     */
    private LocalDateTime randomDateTimeBetween(LocalDate start, LocalDate end) {
        Random random = new Random();
        long days = ChronoUnit.DAYS.between(start, end);
        var time = randomTime();
        return start.atTime(time.getKey(),time.getValue()).plusDays(random.nextInt((int) days + 1));
    }

    /**
     * Génération de dateTime aléatoire entre 2 dates. La date générée ne dois pas être contenu dans la liste des dates interdite
     *
     * @param startDate Dates de départ
     * @param endDate Dates de fin
     * @param startPeriodDate Date de début de la période interdite
     * @param endPeriodDate Dates de fin de la periode interdite
     *
     * @return  Une dateTime alatoire
     */
    private LocalDateTime randomDateBetweenAndNotInPeriod(LocalDate startDate, LocalDate endDate, LocalDate startPeriodDate, LocalDate endPeriodDate) {
        LocalDateTime randomDateTime;

        do {
            randomDateTime =  randomDateTimeBetween(startDate, endDate);

        } while (randomDateTime.toLocalDate().compareTo(startPeriodDate) >= 0 && randomDateTime.toLocalDate().compareTo(endPeriodDate) <= 0);

        return randomDateTime;
    }

    private Map.Entry<Integer, Integer> randomTime() {
        Random random = new Random();
        int hour = random.nextInt(24);
        int min = random.nextInt(59);

        return new AbstractMap.SimpleEntry<>(hour, min);
    }


}
