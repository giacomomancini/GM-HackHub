package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Sottomissione {
    private final String id;
    private final String teamId;
    private String titoloProgetto;
    private String descrizione;
    private String repoURL;
    private LocalDateTime dataInvio;
    private Valutazione valutazione;

    public Sottomissione(String id, String teamId, String titoloProgetto, String descrizione, String repoURL) {
        this.id = Objects.requireNonNull(id);
        this.teamId = Objects.requireNonNull(teamId);
        this.titoloProgetto = Objects.requireNonNull(titoloProgetto);
        this.descrizione = Objects.requireNonNull(descrizione);
        this.repoURL = Objects.requireNonNull(repoURL);
        this.dataInvio = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTitoloProgetto() {
        return titoloProgetto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public Optional<Valutazione> getValutazione() {
        return Optional.ofNullable(valutazione);
    }

    public void assegnaValutazione(Valutazione valutazione) {
        this.valutazione = Objects.requireNonNull(valutazione, "La valutazione non può essere nulla");
    }

    public boolean isValutata() {
        return this.valutazione != null;
    }

    public void aggiorna(String nuovoTitolo, String nuovaDescrizione, String nuovoRepoURL) {
        this.titoloProgetto = nuovoTitolo;
        this.descrizione = nuovaDescrizione;
        this.repoURL = nuovoRepoURL;
        this.dataInvio = LocalDateTime.now();
    }
}
