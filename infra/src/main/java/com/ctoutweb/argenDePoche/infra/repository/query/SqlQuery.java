package com.ctoutweb.argenDePoche.infra.repository.query;

public class SqlQuery {

    /**
     * Requet SQL pour recupérer un compte de famille
     */
    public static final String familyAccountQuery = """
        select
            pfa.family_account_id as family_account_id,
            f.name as family_name,
            (
                select array_agg(distinct p.parent_id)
                from sc_argent_de_poche.parent_family_account p
                where p.family_account_id = pfa.family_account_id
            ) as parent_ids,
            (
                select array_agg(c.id)
                from sc_argent_de_poche.child_account c
                where c.family_account_id = pfa.family_account_id
            ) as child_account_ids
        from sc_argent_de_poche.parent_family_account pfa
        join sc_argent_de_poche.family_account fa on fa.id = pfa.family_account_id
        join sc_argent_de_poche.family f on f.family_account_id = fa.id
        where pfa.parent_id = :parentId
        group by pfa.family_account_id, f.name;
        """;

    /**
     * Requete SQL pour récupérer les données d'un compte d'argent de poche
     */
    public static final String loadChildAccountQuery = """
            select
            ca.id as childMoneyAccountId,
            c.id as childId,
            c.nickname as childName,
            ci.image_name as imageRandomName,
            cac.calendar_period as periodSubscriptionName,
            cac.period_start_day as startPeriodDate,
            cac.period_end_day as endPeriodDate,
            cam.money_at_period_start as moneyAtPeriodStart,
            cam.remaining_money as remainingMoney
            from sc_argent_de_poche.child_account ca
            join sc_argent_de_poche.child c on c.child_account_id = :childAccountId
            join sc_argent_de_poche.child_account_calendar cac on cac.child_account_id = :childAccountId
            join sc_argent_de_poche.child_account_money cam on cam.child_account_id = :childAccountId
            join sc_argent_de_poche.child_image ci on c.child_image_id = ci.id
            where ca.id = :childAccountId;
            """;
}
