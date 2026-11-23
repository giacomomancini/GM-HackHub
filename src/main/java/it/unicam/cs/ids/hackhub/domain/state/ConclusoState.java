package it.unicam.cs.ids.hackhub.domain.state;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.OperazioneNonConsentitaException;
import it.unicam.cs.ids.hackhub.domain.model.Sottomissione;
import it.unicam.cs.ids.hackhub.domain.model.Team;

/**
 * Stato finale dell'Hackathon: l'evento è concluso, il vincitore è proclamato
 * e nessuna ulteriore modifica è permessa.
 */
public class ConclusoState implements HackathonState {

    @Override
    public String getNomeStato() {
        return "CONCLUSO";
    }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        throw new OperazioneNonConsentitaException("L'hackathon è concluso: non è possibile iscrivere team.");
    }

    @Override
    public void inviaSottomissione(Hackathon context, Sottomissione sottomissione) {
        throw new OperazioneNonConsentitaException("L'hackathon è concluso: non è possibile inviare sottomissioni.");
    }

    @Override
    public void valuta(Hackathon context) {
        throw new OperazioneNonConsentitaException("L'hackathon è concluso: le valutazioni sono chiuse.");
    }

    @Override
    public void concludi(Hackathon context) {
        throw new OperazioneNonConsentitaException("L'hackathon è già concluso.");
    }
}
