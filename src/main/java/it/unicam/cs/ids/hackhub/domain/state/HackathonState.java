package it.unicam.cs.ids.hackhub.domain.state;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.Team;
import it.unicam.cs.ids.hackhub.domain.model.Sottomissione;

/**
 * State Pattern: Interfaccia astratta per il ciclo di vita dell'Hackathon.
 */
public interface HackathonState {
    String getNomeStato();

    void iscriviTeam(Hackathon context, Team team);

    void inviaSottomissione(Hackathon context, Sottomissione sottomissione);

    void valuta(Hackathon context);

    void concludi(Hackathon context);
}
