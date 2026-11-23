package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;

/**
 * Adapter Pattern - Interfaccia Target per il servizio di Calendar:
 * Definisce il contratto interno della piattaforma per creare slot di call.
 */
public interface CalendarService {
    String creaSlotCall(String idMentore, String idTeam, LocalDateTime dataOra);
}
