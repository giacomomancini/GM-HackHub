package it.unicam.cs.ids.hackhub.domain.state;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.OperazioneNonConsentitaException;
import it.unicam.cs.ids.hackhub.domain.model.Team;
import it.unicam.cs.ids.hackhub.domain.model.Sottomissione;

/**
 * Stato iniziale dell'Hackathon: le iscrizioni sono aperte.
 */
public class InIscrizioneState implements HackathonState {

    @Override
    public String getNomeStato() {
        return "IN_ISCRIZIONE";
    }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        context.aggiungiTeamIscritto(team);
    }

    @Override
    public void inviaSottomissione(Hackathon context, Sottomissione sottomissione) {
        throw new OperazioneNonConsentitaException("Non è possibile inviare sottomissioni: l'hackathon è ancora in fase di iscrizione.");
    }

    @Override
    public void valuta(Hackathon context) {
        throw new OperazioneNonConsentitaException("Non è possibile valutare: l'hackathon non è ancora iniziato.");
    }

    @Override
    public void concludi(Hackathon context) {
        throw new OperazioneNonConsentitaException("Non è possibile concludere un hackathon in fase di iscrizione.");
    }
}
