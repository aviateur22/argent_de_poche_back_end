package com.ctoutweb.argenDePoche.infra.repository.query;

public class CmdRepositoryQuery {

    /**
     * Requet SQL pour recupérer un compte de famille
     */
    public static final String familyAccountQuery = """
        select
            pfa.family_account_id as family_account_id,
            fa.name as family_name,
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
        where pfa.parent_id = :parentId
        group by pfa.family_account_id, fa.name;
        """;

    /**
     * Requet SQL pour recupérer un compte d'argent de poche
     */
    public static final String childAccountQuery = """
              select
                   c.child_account_id       as child_account_id,
                   c.image_name             as image_name,
                   c.nickname               as child_name,
                   cam.money_at_period_start as start_money,
                   cam.remaining_money       as remaining_money,
                   cac.calendar_period       as calendar_period,
                   cac.period_end_day        as period_end_day,
                   cac.period_start_day      as period_start_day
              from sc_argent_de_poche.child c
              join sc_argent_de_poche.child_account_calendar cac
                   on cac.child_account_id = c.child_account_id
              join sc_argent_de_poche.child_account_money cam
                   on cam.child_account_id = c.child_account_id
              where c.child_account_id = :childAccountId
        """;

    public static final String generateNextChildAccountIdentificationQuery = """
            SELECT
                nextval('sc_argent_de_poche.child_account_id_seq') AS next_child_money_account_id,
                nextval('sc_argent_de_poche.child_id_seq') AS next_child_id
            """;

    public static final String registerNewChildAccount = """
        with child_account as (
            insert into sc_argent_de_poche.child_account (id, family_account_id) values (:childAccountId, :familyAccountId) returning id
        ),
        insert_child as (
            insert into sc_argent_de_poche.child (id, child_account_id, nickname, image_name)
            SELECT
                :childId,
                id,
                :childName,
                :childImage
            FROM child_account)
        select id from child_account;
       """;
}
