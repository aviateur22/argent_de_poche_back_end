package com.ctoutweb.argentDePoche.application.query.dto.query;

import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.dto.QrCodeDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record GenerateQrCodeQuery(
        String url,
        int imageHeightAndWidth,
        ParentIdentity parentIdentity,
        ChildMoneyAccountIdentity childMoneyAccountIdentity) implements Query<QrCodeDto> {
  /**
   * Instancie GenerateQrCodeQuery
   *
   * @param url L'url a afficher dans le QR code
   * @param imageHeightAndWidth
   *
   * @return Nouvelle instance
   */
  public static GenerateQrCodeQuery create(
          String url,
          int imageHeightAndWidth,
          ParentIdentity parentIdentity,
          ChildMoneyAccountIdentity childMoneyAccountIdentity) {
    return new GenerateQrCodeQuery(url, imageHeightAndWidth, parentIdentity, childMoneyAccountIdentity);
  }
}
