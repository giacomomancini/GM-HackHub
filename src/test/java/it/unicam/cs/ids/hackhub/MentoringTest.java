package it.unicam.cs.ids.hackhub;

import it.unicam.cs.ids.hackhub.controller.MentoringController;
import it.unicam.cs.ids.hackhub.domain.model.*;
import it.unicam.cs.ids.hackhub.domain.state.InIscrizioneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MentoringTest {

    private MentoringController controller;
    private Hackathon hackathon;
    private Mentore mentoreAssegnato;
    private Mentore altroMentore;
    private RichiestaSupporto richiesta;

    @BeforeEach
    void setUp() {
        SistemaCalendarEsterno calendarEsterno = new SistemaCalendarEsterno();
        CalendarService adapter = new CalendarAdapter(calendarEsterno);
        controller = new MentoringController(adapter);

        Organizzatore org = new Organizzatore("org-1", "Mario Rossi", "mario@unicam.it", "pwd");
        Giudice giudice = new Giudice("giu-1", "Luigi Bianchi", "luigi@unicam.it", "pwd");
        mentoreAssegnato = new Mentore("men-1", "Anna Verdi", "anna@unicam.it", "pwd");
        altroMentore = new Mentore("men-99", "Mentore Esterno", "esterno@unicam.it", "pwd");

        LocalDateTime ora = LocalDateTime.now();
        hackathon = new Hackathon(
                "h-1",
                "HackHub 2026",
                "Regolamento",
                ora.plusDays(1),
                ora.plusDays(2),
                ora.plusDays(4),
                "Camerino",
                1500.0,
                5,
                org,
                giudice,
                List.of(mentoreAssegnato),
                new InIscrizioneState()
        );

        richiesta = new RichiestaSupporto(
                "req-1",
                "team-1",
                "Problema configurazione database",
                "Non riusciamo a far comunicare l'app con il DB"
        );
        hackathon.aggiungiRichiestaSupporto(richiesta);
    }

    @Test
    @DisplayName("UC-M2 Ramo Eccezione 1.2: Errore se hackathon non in stato IN_CORSO")
    void testErroreSeNonInCorso() {
        LocalDateTime dataCall = LocalDateTime.now().plusDays(2).withHour(15).withMinute(0);

        assertThrows(OperazioneNonConsentitaException.class, () ->
                controller.proponiCall(hackathon, mentoreAssegnato, richiesta.getId(), dataCall)
        );
    }

    @Test
    @DisplayName("UC-M2 Ramo Eccezione 1.2: Errore se il mentore non è assegnato all'hackathon")
    void testErroreMentoreNonAssegnato() {
        hackathon.avanzaAInCorso();
        LocalDateTime dataCall = LocalDateTime.now().plusDays(2).withHour(15).withMinute(0);

        assertThrows(OperazioneNonConsentitaException.class, () ->
                controller.proponiCall(hackathon, altroMentore, richiesta.getId(), dataCall)
        );
    }

    @Test
    @DisplayName("UC-M2 Flusso Principale: Proposta call con delega a Calendar e richiesta IN_GESTIONE")
    void testPropostaCallSuccesso() {
        hackathon.avanzaAInCorso();
        LocalDateTime dataCall = LocalDateTime.now().plusDays(2).withHour(15).withMinute(0);

        PropostaCall proposta = controller.proponiCall(hackathon, mentoreAssegnato, richiesta.getId(), dataCall);

        assertNotNull(proposta);
        assertEquals(mentoreAssegnato, proposta.getMentore());
        assertNotNull(proposta.getLinkPrenotazione());
        assertTrue(proposta.getLinkPrenotazione().contains("https://calendar.external-service.org/booking/"));
        assertEquals(RichiestaSupporto.StatoRichiesta.IN_GESTIONE, richiesta.getStato());
        assertTrue(richiesta.getPropostaCall().isPresent());
    }
}
