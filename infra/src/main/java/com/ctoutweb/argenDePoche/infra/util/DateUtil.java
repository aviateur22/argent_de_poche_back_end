package com.ctoutweb.argenDePoche.infra.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {
  /**
   * Convertion d'une LocalDateTime to ZonedDateTime with specific ZoneId
   *
   * @param zoneId
   * @param localDateTime
   *
   * @return ZonedDateTime
   */
  public static ZonedDateTime localDateTimeToZonedDateTime(ZoneId zoneId, LocalDateTime localDateTime) {
    return localDateTime.atZone(zoneId);
  }

  /**
   * Convertion d'une ZoneDateTime UTC to ZonedDateTime with specific ZoneId
   *
   * @param zoneId Identitifiant de la zone souhaité
   * @param utcTime Heure en UTC
   *
   * @return ZonedDateTime
   */
  public static ZonedDateTime uctToZonedDateTime(ZoneId zoneId, ZonedDateTime utcTime) {
    if(utcTime == null)
      return null;

    return utcTime.withZoneSameInstant(zoneId);
  }

  /**
   * Formatte la date en date et HHmm
   *
   * @param time - LocalDateTime
   *
   * @return String
   */
  public static String toDateHour(LocalDateTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    return time.format(formatter);
  }

  /**
   * Format la date en dd/MM/yyyy
   * @param localDate  LocalDate
   * @return String
   */
  public static String formatToDdMmYy(LocalDate localDate) {
    if(localDate == null)
      return null;

    return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE));
  }

  /**
   * Convertie une String dd/MM/yyyy en LocalDate
   * @param localDate String - Text avec la date
   * @return LocalDate
   */
  public static LocalDate convertStringDdMmYyToLocalDate(String localDate) {
    if(localDate == null)
      return null;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(localDate, formatter);
  }
}
