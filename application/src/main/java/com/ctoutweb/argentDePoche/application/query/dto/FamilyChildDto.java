package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

/**
 * Les enfant de la famille
 *
 * @param name Le nom de l'enfant
 * @param imageRandomName Le nom de l'image
 */
public record FamilyChildDto(
        ChildMoneyAccountIdentity childMoneyAccountIdentity,
        String name,
        String imageRandomName) {
}
