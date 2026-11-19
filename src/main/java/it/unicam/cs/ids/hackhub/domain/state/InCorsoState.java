package it.unicam.cs.ids.hackhub.domain.state;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.OperazioneNonConsentitaException;
import it.unicam.cs.ids.hackhub.domain.model.Sottomissione;
import it.unicam.cs.ids.hackhub.domain.model.Team;

/**
 * Stato dell'Hackathon durante lo svolgimento: le sottomissioni sono aperte.
 */
public class InCorsoState implements HackathonState {

    @Override
    public String getNomeStato() {
        return "IN_CORSO";
    }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        throw new OperazioneNonConsentitaException("Le iscrizioni sono chiuse: l'hackathon è già in corso.");
    }

    @Override
    public void inviaSottomissione(Hackathon context, Sottomissione sottomissione) {
        context.aggiungiSottomissione(sottomissione);
    }

    @Override
    public void valuta(Hackathon context) {
        throw new OperazioneNonConsentitaException("Non è possibile valutare finché l'hackathon è in corso.");
    }

    @Override
    public void concludi(Hackathon context) {
        throw new OperazioneNonConsentitaException("Non è possibile concludere l'hackathon mentre è in corso.");
    }
}
