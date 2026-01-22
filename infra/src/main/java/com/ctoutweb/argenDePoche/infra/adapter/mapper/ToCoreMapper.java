package com.ctoutweb.argenDePoche.infra.adapter.mapper;

import com.ctoutweb.argenDePoche.infra.repository.dto.FamilyAccountProjection;
import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.port.NextFamilyAccountIdentities;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.Family;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ToCoreMapper {

    /**
     * Map vers l'identifiant d'un parent
     *
     * @param technicalId L'identification technique du parent
     *
     * @return L'identifiant du parent pour le domaine
     */
    public ParentIdentity toParentIdentity(long technicalId) {
        return new ParentIdentity(technicalId);
    }

    /**
     * Map vers l'identifiant d'un enfant
     *
     * @param childId L'identification technique de l'enfant
     *
     * @return L'identifiant de l'enfant pour le domaine
     */
    public ChildIdentity toChildIdentity(long childId) {
        return new ChildIdentity(childId);
    }

    /**
     * Map vers l'identifiant d'un compte d'argent de poche
     *
     * @param childAccountId L'identification technique du compte d'argent de poche
     *
     * @return L'identifiant du compte d'argent de poche pour le domaine
     */
    public ChildMoneyAccountIdentity toChildAccountIdentity(long childAccountId) {
        return new ChildMoneyAccountIdentity(childAccountId);
    }

    /**
     * Map vers l'aggregat FamilyAccount
     *
     * @param projection Les données issue de la base de données
     *
     * @return L'aggégat
     */
    public FamilyAccount toCoreFamilyAccount(FamilyAccountProjection projection) {
        if(projection == null || projection.familyAccountId() == null)
            return null;

        return new FamilyAccount(
                    mapToFamilyAccountIdentity(projection.familyAccountId()),
                    new Family(
                            mapToFamilyIdentity(projection.familyAccountId()),
                            projection.familyName()
                    ),
                    projection.parentIds()
                            .stream()
                            .map(this::mapToParentIdentity)
                            .toList(),
                    projection.childAccountIds()
                            .stream()
                            .map(this::mapToChildMoneyAccountIdentity)
                            .toList()
                );
    }

    /**
     * Map les identifiants techniques générés vers le domaine
     *
     * @param nextChildAccountId L'identifiant technique généré pour le compte d'argent de poche
     * @param nextChildId L'identifiant tecgnique généré pour l'enfant
     *
     * @return Les identifiants technique mappé pour le domaine
     */
    public NextChildAccountIdentities toNextChildAccountIdentities(long nextChildAccountId, long nextChildId) {
        return new NextChildAccountIdentities() {
            @Override
            public ChildIdentity getNextChildIdentity() {
                return toChildIdentity(nextChildId);
            }

            @Override
            public ChildMoneyAccountIdentity getNextChildMoneyAccountId() {
                return toChildAccountIdentity(nextChildAccountId);
            }
        };
    }

    public NextFamilyAccountIdentities toNextFamilyIdentities(long nextFamilyAccountId, long nextFamilyId, long nextParentId) {
        return new NextFamilyAccountIdentities() {
            @Override
            public FamilyAccountIdentity getNextFamilyAccountIdentity() {
                return mapToFamilyAccountIdentity(nextFamilyAccountId);
            }

            @Override
            public FamilyIdentity getNexFamilyIdentity() {
                return mapToFamilyIdentity(nextFamilyId);
            }

            @Override
            public ParentIdentity getNextParentIdentity() {
                return mapToParentIdentity(nextParentId);
            }
        };
    }
    private FamilyAccountIdentity mapToFamilyAccountIdentity(long familyAccountId) {
        return new FamilyAccountIdentity(familyAccountId);
    }

    private FamilyIdentity mapToFamilyIdentity(long familyId) {
        return new FamilyIdentity(familyId);
    }

    private ParentIdentity mapToParentIdentity(long parentId) {
        return new ParentIdentity(parentId);
    }

    private ChildMoneyAccountIdentity mapToChildMoneyAccountIdentity(long childAccountId) {
        return new ChildMoneyAccountIdentity(childAccountId);
    }

}
