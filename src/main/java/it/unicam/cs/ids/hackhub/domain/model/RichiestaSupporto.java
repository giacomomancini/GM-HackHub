package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Rappresenta una richiesta di supporto inviata da un Team durante l'Hackathon.
 */
public class RichiestaSupporto {

    public enum StatoRichiesta {
        APERTA,
        IN_GESTIONE,
        RISOLTA
    }

    private final String id;
    private final String teamId;
    private final String oggetto;
    private final String messaggio;
    private final LocalDateTime dataCreazione;
    private StatoRichiesta stato;
    private PropostaCall propostaCall;

    public RichiestaSupporto(String id, String teamId, String oggetto, String messaggio) {
        this.id = Objects.requireNonNull(id, "L'id della richiesta non può essere nullo");
        this.teamId = Objects.requireNonNull(teamId, "L'id del team non può essere nullo");
        this.oggetto = Objects.requireNonNull(oggetto, "L'oggetto non può essere nullo");
        this.messaggio = Objects.requireNonNull(messaggio, "Il messaggio non può essere nullo");
        this.dataCreazione = LocalDateTime.now();
        this.stato = StatoRichiesta.APERTA;
    }

    public String getId() {
        return id;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public boolean isAperta() {
        return this.stato == StatoRichiesta.APERTA;
    }

    public Optional<PropostaCall> getPropostaCall() {
        return Optional.ofNullable(propostaCall);
    }

    public void impostaInGestione(PropostaCall propostaCall) {
        this.propostaCall = Objects.requireNonNull(propostaCall, "La proposta di call non può essere nulla");
        this.stato = StatoRichiesta.IN_GESTIONE;
    }

    public void segnaComeRisolta() {
        this.stato = StatoRichiesta.RISOLTA;
    }
}
