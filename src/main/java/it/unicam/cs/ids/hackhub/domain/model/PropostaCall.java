package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rappresenta la proposta di una call di supporto avanzata da un Mentore per un Team.
 */
public class PropostaCall {
    private final LocalDateTime dataOraProposta;
    private final String linkPrenotazione;
    private final Mentore mentore;

    public PropostaCall(LocalDateTime dataOraProposta, String linkPrenotazione, Mentore mentore) {
        this.dataOraProposta = Objects.requireNonNull(dataOraProposta, "La data/ora della call non può essere nulla");
        this.linkPrenotazione = Objects.requireNonNull(linkPrenotazione, "Il link di prenotazione non può essere nullo");
        this.mentore = Objects.requireNonNull(mentore, "Il mentore non può essere nullo");
    }

    public LocalDateTime getDataOraProposta() {
        return dataOraProposta;
    }

    public String getLinkPrenotazione() {
        return linkPrenotazione;
    }

    public Mentore getMentore() {
        return mentore;
    }
}
