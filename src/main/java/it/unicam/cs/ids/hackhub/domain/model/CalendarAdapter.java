package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Adapter Pattern - Concrete Adapter per Calendar:
 * Adatta l'interfaccia CalendarService alle API di SistemaCalendarEsterno.
 */
public class CalendarAdapter implements CalendarService {

    private final SistemaCalendarEsterno servizioEsterno;

    public CalendarAdapter(SistemaCalendarEsterno servizioEsterno) {
        this.servizioEsterno = Objects.requireNonNull(servizioEsterno, "Il servizio calendar esterno non può essere nullo");
    }

    @Override
    public String creaSlotCall(String idMentore, String idTeam, LocalDateTime dataOra) {
        if (idMentore == null || idTeam == null || dataOra == null) {
            return null;
        }
        SistemaCalendarEsterno.RisultatoSlot risultato = servizioEsterno.prenotaSlot(
                "mentor-" + idMentore + "@hackhub.it",
                "team-" + idTeam + "@hackhub.it",
                dataOra
        );
        return risultato.successo() ? risultato.bookingUrl() : null;
    }
}
