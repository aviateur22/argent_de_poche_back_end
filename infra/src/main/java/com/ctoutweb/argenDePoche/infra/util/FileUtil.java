package com.ctoutweb.argenDePoche.infra.util;

import org.springframework.http.codec.multipart.FilePart;

public class FileUtil {
  private FileUtil() {
    throw new IllegalStateException("Class utilitaire");
  }
  /**
   * Renvoie l'extension de l'image
   *
   * @param childImageFile L'image recu par le client
   *
   * @return L'extension
   */
  public static String getFileExtension(FilePart childImageFile) {
    var initialFileName = childImageFile.filename();
    // Extract extension
    String extension = "";

    int dotIndex = initialFileName.lastIndexOf(".");
    if (dotIndex > 0) {
      extension = initialFileName.substring(dotIndex);
    }

    /**
     * Renvoie l'extension en supprimant le . en 1ere position
     */
    return extension.substring(1);
  }
}
