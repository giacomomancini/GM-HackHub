package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;

/**
 * Adapter Pattern - Adaptee per Calendar:
 * Simula il servizio esterno proprietario di gestione calendario (es. Google Calendar API).
 */
public class SistemaCalendarEsterno {

    public record RisultatoSlot(boolean successo, String bookingUrl) {}

    public RisultatoSlot prenotaSlot(String organizerEmail, String attendeeEmail, LocalDateTime startTime) {
        if (organizerEmail == null || attendeeEmail == null || startTime == null) {
            return new RisultatoSlot(false, null);
        }
        String meetingId = "meet-" + System.currentTimeMillis();
        return new RisultatoSlot(true, "https://calendar.external-service.org/booking/" + meetingId);
    }
}
