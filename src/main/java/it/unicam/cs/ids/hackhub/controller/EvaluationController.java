package it.unicam.cs.ids.hackhub.controller;

import it.unicam.cs.ids.hackhub.domain.model.*;

import java.util.Objects;

/**
 * Controller per la realizzazione del caso d'uso UC-G1: Valutazione Sottomissione.
 */
public class EvaluationController {

    public Valutazione valuta(Hackathon hackathon,
                              Giudice giudice,
                              String idSottomissione,
                              double voto,
                              String giudizio) {
        Objects.requireNonNull(hackathon, "L'hackathon non può essere nullo");
        Objects.requireNonNull(giudice, "Il giudice non può essere nullo");
        Objects.requireNonNull(idSottomissione, "L'id sottomissione non può essere nullo");

        validaPunteggio(voto);

        if (!hackathon.isValutazioneAperta(giudice.getId())) {
            throw new OperazioneNonConsentitaException(
                    "Operazione non consentita: l'hackathon non è in fase di valutazione oppure il giudice non è autorizzato."
            );
        }

        Sottomissione sottomissione = hackathon.trovaSottomissionePerId(idSottomissione);
        if (sottomissione == null) {
            throw new IllegalArgumentException("Sottomissione non trovata con id: " + idSottomissione);
        }

        Valutazione nuovaValutazione = new Valutazione(voto, giudizio, giudice);
        sottomissione.assegnaValutazione(nuovaValutazione);

        return nuovaValutazione;
    }

    public void validaPunteggio(double voto) {
        if (voto < 0.0 || voto > 10.0) {
            throw new IllegalArgumentException("Il punteggio deve essere compreso tra 0 e 10. Ricevuto: " + voto);
        }
    }
}
