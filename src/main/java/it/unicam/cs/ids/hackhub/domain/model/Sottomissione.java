package it.unicam.cs.ids.hackhub.domain.model;

import java.time.LocalDateTime;

public class Sottomissione {
    private final String id;
    private final String teamId;
    private String titoloProgetto;
    private String descrizione;
    private String repoURL;
    private LocalDateTime dataInvio;

    public Sottomissione(String id, String teamId, String titoloProgetto, String descrizione, String repoURL) {
        this.id = id;
        this.teamId = teamId;
        this.titoloProgetto = titoloProgetto;
        this.descrizione = descrizione;
        this.repoURL = repoURL;
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

    public void aggiorna(String nuovoTitolo, String nuovaDescrizione, String nuovoRepoURL) {
        this.titoloProgetto = nuovoTitolo;
        this.descrizione = nuovaDescrizione;
        this.repoURL = nuovoRepoURL;
        this.dataInvio = LocalDateTime.now();
    }
}
