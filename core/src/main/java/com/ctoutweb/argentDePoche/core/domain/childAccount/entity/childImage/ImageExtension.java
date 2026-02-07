package com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage;

public enum ImageExtension {
  JPEG("jpg"),
  PNG("png"),
  SVG("svg");

  private final String extensionText;

  private ImageExtension(String extensionText) {
    this.extensionText = extensionText;
  }

  public String getFileExtensionText() {
    return this.extensionText;
  }
}
