package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rappresenta la valutazione rilasciata da un Giudice su una specifica Sottomissione.
 * Il punteggio deve essere compreso tra 0 e 10.
 */
public class Valutazione {
    private final double punteggio;
    private final String giudizio;
    private final Giudice giudice;
    private final LocalDateTime dataValutazione;

    public Valutazione(double punteggio, String giudizio, Giudice giudice) {
        if (punteggio < 0.0 || punteggio > 10.0) {
            throw new IllegalArgumentException("Il punteggio deve essere compreso tra 0 e 10. Ricevuto: " + punteggio);
        }
        if (giudizio == null || giudizio.isBlank()) {
            throw new IllegalArgumentException("Il giudizio scritto è obbligatorio.");
        }
        this.punteggio = punteggio;
        this.giudizio = giudizio;
        this.giudice = Objects.requireNonNull(giudice, "Il giudice non può essere nullo");
        this.dataValutazione = LocalDateTime.now();
    }

    public double getPunteggio() {
        return punteggio;
    }

    public String getGiudizio() {
        return giudizio;
    }

    public Giudice getGiudice() {
        return giudice;
    }

    public LocalDateTime getDataValutazione() {
        return dataValutazione;
    }
}
