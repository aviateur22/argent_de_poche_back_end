package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;

/**
 * Les enfant de la famille
 *
 * @param childIdentity l'identity de l'enfant
 * @param name Le nom de l'enfant
 * @param imageRandomName Le nom de l'image
 * @param imagePath Le path de stockage
 */
public record FamilyChildDto(
        ChildIdentity childIdentity,
        String name,
        String imageRandomName,
        String imagePath) {
}
