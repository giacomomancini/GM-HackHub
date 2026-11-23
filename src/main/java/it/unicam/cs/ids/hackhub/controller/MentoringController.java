package it.unicam.cs.ids.hackhub.controller;

import it.unicam.cs.ids.hackhub.domain.model.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Controller per la realizzazione del caso d'uso UC-M2: Proposta Call a Team.
 */
public class MentoringController {

    private final CalendarService calendarService;

    public MentoringController(CalendarService calendarService) {
        this.calendarService = Objects.requireNonNull(calendarService, "Il servizio calendar non può essere nullo");
    }

    public PropostaCall proponiCall(Hackathon hackathon,
                                   Mentore mentore,
                                   String idRichiesta,
                                   LocalDateTime dataOraProposta) {
        Objects.requireNonNull(hackathon, "L'hackathon non può essere nullo");
        Objects.requireNonNull(mentore, "Il mentore non può essere nullo");
        Objects.requireNonNull(idRichiesta, "L'id della richiesta non può essere nullo");
        Objects.requireNonNull(dataOraProposta, "La data e ora proposta non possono essere nulle");

        if (!hackathon.isSupportoAperto(mentore.getId())) {
            throw new OperazioneNonConsentitaException(
                    "Operazione non consentita: il supporto non è attivo oppure il mentore non è assegnato all'hackathon."
            );
        }

        RichiestaSupporto richiesta = hackathon.trovaRichiestaSupportoPerId(idRichiesta);
        if (richiesta == null) {
            throw new IllegalArgumentException("Richiesta di supporto non trovata con id: " + idRichiesta);
        }

        if (!richiesta.isAperta()) {
            throw new OperazioneNonConsentitaException(
                    "Operazione non consentita: la richiesta di supporto non è nello stato APERTA."
            );
        }

        String linkPrenotazione = calendarService.creaSlotCall(
                mentore.getId(),
                richiesta.getTeamId(),
                dataOraProposta
        );

        if (linkPrenotazione == null || linkPrenotazione.isBlank()) {
            throw new OperazioneNonConsentitaException(
                    "Errore nella delega al servizio Calendar: impossibile generare lo slot di prenotazione."
            );
        }

        PropostaCall proposta = new PropostaCall(dataOraProposta, linkPrenotazione, mentore);
        richiesta.impostaInGestione(proposta);

        return proposta;
    }
}
