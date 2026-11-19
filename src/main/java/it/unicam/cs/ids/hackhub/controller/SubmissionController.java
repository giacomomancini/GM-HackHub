package it.unicam.cs.ids.hackhub.controller;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.OperazioneNonConsentitaException;
import it.unicam.cs.ids.hackhub.domain.model.Sottomissione;

import java.util.Objects;
import java.util.UUID;

/**
 * Controller per la realizzazione del caso d'uso UC-T2: Inoltro Sottomissione.
 */
public class SubmissionController {

    public record DatiSottomissione(
            String titoloProgetto,
            String descrizione,
            String repoURL
    ) {}

    public Sottomissione inviaSottomissione(Hackathon hackathon, String teamId, DatiSottomissione dati) {
        Objects.requireNonNull(hackathon, "L'hackathon non può essere nullo");
        Objects.requireNonNull(teamId, "L'id del team non può essere nullo");
        validaDatiSottomissione(dati);

        if (!hackathon.isInvioAperto()) {
            throw new OperazioneNonConsentitaException(
                    "Operazione non consentita: le consegne per l'hackathon '" + hackathon.getNome() + "' non sono aperte."
            );
        }

        Sottomissione esistente = hackathon.getSottomissione(teamId);
        if (esistente != null) {
            esistente.aggiorna(dati.titoloProgetto(), dati.descrizione(), dati.repoURL());
            return esistente;
        }

        String idSottomissione = UUID.randomUUID().toString();
        Sottomissione nuovaSottomissione = new Sottomissione(
                idSottomissione,
                teamId,
                dati.titoloProgetto(),
                dati.descrizione(),
                dati.repoURL()
        );

        hackathon.aggiungiSottomissione(nuovaSottomissione);
        return nuovaSottomissione;
    }

    private void validaDatiSottomissione(DatiSottomissione dati) {
        if (dati == null) {
            throw new IllegalArgumentException("I dati della sottomissione sono obbligatori.");
        }
        if (dati.titoloProgetto() == null || dati.titoloProgetto().isBlank()) {
            throw new IllegalArgumentException("Il titolo del progetto è obbligatorio.");
        }
        if (dati.repoURL() == null || dati.repoURL().isBlank()) {
            throw new IllegalArgumentException("L'URL del repository è obbligatorio.");
        }
    }
}
