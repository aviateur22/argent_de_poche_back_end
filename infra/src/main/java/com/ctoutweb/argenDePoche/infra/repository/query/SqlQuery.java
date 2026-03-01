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

    public static final String getAvailableMovementReasonsByChildAccount =
        """
        select movement_name, movement_code
        from sc_argent_de_poche.child_account_movement_code camc 
        join sc_argent_de_poche.movement_action_code mac on camc.movement_code_id = mac.id
        where camc.child_account_id = :childAccountid;
        """;

    public static final String getAllParentRoles =
            """
            select role from sc_argent_de_poche.role_parent rp
            join sc_argent_de_poche."role" r
            on r.id = rp.role_id
            where rp.parent_id = :parentId
            """;
}
