package it.unicam.cs.ids.hackhub.domain.state;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.OperazioneNonConsentitaException;
import it.unicam.cs.ids.hackhub.domain.model.Sottomissione;
import it.unicam.cs.ids.hackhub.domain.model.Team;

/**
 * Stato dell'Hackathon dopo il termine della gara: fase di valutazione attiva.
 */
public class InValutazioneState implements HackathonState {

    @Override
    public String getNomeStato() {
        return "IN_VALUTAZIONE";
    }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        throw new OperazioneNonConsentitaException("Le iscrizioni sono chiuse: l'hackathon è in fase di valutazione.");
    }

    @Override
    public void inviaSottomissione(Hackathon context, Sottomissione sottomissione) {
        throw new OperazioneNonConsentitaException("Le consegne sono chiuse: l'hackathon è in fase di valutazione.");
    }

    @Override
    public void valuta(Hackathon context) {
        // Operazione consentita in questo stato
    }

    @Override
    public void concludi(Hackathon context) {
        // La transizione a Concluso avverrà quando tutte le sottomissioni saranno valutate
    }
}
