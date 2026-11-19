package it.unicam.cs.ids.hackhub;

import it.unicam.cs.ids.hackhub.controller.SubmissionController;
import it.unicam.cs.ids.hackhub.controller.SubmissionController.DatiSottomissione;
import it.unicam.cs.ids.hackhub.domain.model.*;
import it.unicam.cs.ids.hackhub.domain.state.InIscrizioneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionTest {

    private SubmissionController controller;
    private Hackathon hackathon;
    private Team team;

    @BeforeEach
    void setUp() {
        controller = new SubmissionController();
        Organizzatore org = new Organizzatore("org-1", "Mario Rossi", "mario@unicam.it", "pwd");
        Giudice giudice = new Giudice("giu-1", "Luigi Bianchi", "luigi@unicam.it", "pwd");
        List<Mentore> mentori = List.of(new Mentore("men-1", "Anna Verdi", "anna@unicam.it", "pwd"));

        LocalDateTime ora = LocalDateTime.now();
        hackathon = new Hackathon(
                "h-1",
                "HackHub 2026",
                "Regolamento",
                ora.plusDays(1),
                ora.plusDays(2),
                ora.plusDays(4),
                "Camerino",
                1000.0,
                5,
                org,
                giudice,
                mentori,
                new InIscrizioneState()
        );

        Utente leader = new Utente("u-1", "Dev Team Lead", "lead@test.it", "pwd");
        team = new Team("t-1", "Coders", leader);
        hackathon.aggiungiTeamIscritto(team);
    }

    @Test
    @DisplayName("UC-T2 Ramo Eccezione 1.2: Errore se hackathon non in stato IN_CORSO")
    void testErroreSeNonInCorso() {
        DatiSottomissione dati = new DatiSottomissione(
                "Progetto HackHub",
                "Piattaforma web innovativa",
                "https://github.com/team/hackhub"
        );

        // L'hackathon è ancora in IN_ISCRIZIONE
        assertThrows(OperazioneNonConsentitaException.class, () ->
                controller.inviaSottomissione(hackathon, team.getId(), dati)
        );
    }

    @Test
    @DisplayName("UC-T2 Flusso Principale 1.3-1.7: Invio sottomissione con successo quando IN_CORSO")
    void testInvioSottomissioneSuccesso() {
        // Portiamo l'hackathon nello stato IN_CORSO
        hackathon.avanzaAInCorso();

        DatiSottomissione dati = new DatiSottomissione(
                "Progetto HackHub",
                "Descrizione progetto",
                "https://github.com/team/hackhub"
        );

        Sottomissione s = controller.inviaSottomissione(hackathon, team.getId(), dati);

        assertNotNull(s);
        assertEquals("Progetto HackHub", s.getTitoloProgetto());
        assertEquals("https://github.com/team/hackhub", s.getRepoURL());
        assertEquals(team.getId(), s.getTeamId());
        assertEquals(1, hackathon.getSottomissioni().size());
    }

    @Test
    @DisplayName("UC-T3 Aggiornamento: Se la sottomissione esiste già viene aggiornata")
    void testAggiornamentoSottomissioneEsistente() {
        hackathon.avanzaAInCorso();

        DatiSottomissione primaVersione = new DatiSottomissione(
                "Bozza",
                "Prima bozza",
                "https://github.com/team/hackhub"
        );
        controller.inviaSottomissione(hackathon, team.getId(), primaVersione);

        DatiSottomissione versioneFinale = new DatiSottomissione(
                "Titolo Finale",
                "Descrizione completa",
                "https://github.com/team/hackhub-v2"
        );
        Sottomissione aggiornata = controller.inviaSottomissione(hackathon, team.getId(), versioneFinale);

        assertEquals("Titolo Finale", aggiornata.getTitoloProgetto());
        assertEquals(1, hackathon.getSottomissioni().size(), "Non deve duplicare le sottomissioni per lo stesso team");
    }
}
