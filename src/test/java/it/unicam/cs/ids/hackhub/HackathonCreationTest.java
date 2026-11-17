package it.unicam.cs.ids.hackhub;

import it.unicam.cs.ids.hackhub.controller.HackathonCreationController;
import it.unicam.cs.ids.hackhub.controller.HackathonCreationController.DatiCreazioneHackathon;
import it.unicam.cs.ids.hackhub.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HackathonCreationTest {

    private HackathonCreationController controller;
    private Organizzatore organizzatore;
    private Giudice giudice;
    private List<Mentore> mentori;

    @BeforeEach
    void setUp() {
        controller = new HackathonCreationController();
        organizzatore = new Organizzatore("org-1", "Mario Rossi", "mario@unicam.it", "pwd123");
        giudice = new Giudice("giu-1", "Luigi Bianchi", "luigi@unicam.it", "pwd123");
        mentori = List.of(
                new Mentore("men-1", "Anna Verdi", "anna@unicam.it", "pwd123"),
                new Mentore("men-2", "Paolo Neri", "paolo@unicam.it", "pwd123")
        );
    }

    @Test
    @DisplayName("UC-O1 Flusso Principale: Creazione Hackathon con dati validi")
    void testCreazioneHackathonSuccesso() {
        LocalDateTime ora = LocalDateTime.now();
        DatiCreazioneHackathon dati = new DatiCreazioneHackathon(
                "h-2026-01",
                "HackHub UNICAM Challenge",
                "Regolamento ufficiale di gara...",
                ora.plusDays(7),
                ora.plusDays(10),
                ora.plusDays(12),
                "Camerino (MC)",
                1500.0,
                5
        );

        Hackathon h = controller.creaHackathon(dati, organizzatore, giudice, mentori);

        assertNotNull(h);
        assertEquals("h-2026-01", h.getId());
        assertEquals("IN_ISCRIZIONE", h.getStato().getNomeStato());
        assertEquals(giudice, h.getGiudice());
        assertEquals(2, h.getMentori().size());
        assertTrue(controller.trovaPerId("h-2026-01").isPresent());
    }

    @Test
    @DisplayName("UC-O1 Flusso Alternativo 6a: Errore date cronologicamente inconsistenti")
    void testErroreInconsistenzaDate() {
        LocalDateTime ora = LocalDateTime.now();
        // Scadenza iscrizioni DOPO la data di inizio
        DatiCreazioneHackathon datiErrati = new DatiCreazioneHackathon(
                "h-err",
                "Hack Errato",
                "Regolamento",
                ora.plusDays(15), // Scadenza dopo inizio!
                ora.plusDays(10),
                ora.plusDays(12),
                "Online",
                1000.0,
                4
        );

        assertThrows(IllegalArgumentException.class, () ->
                controller.creaHackathon(datiErrati, organizzatore, giudice, mentori));
    }

    @Test
    @DisplayName("UC-O1 Flusso Alternativo 6b: Errore staff senza mentori")
    void testErroreStaffSenzaMentori() {
        LocalDateTime ora = LocalDateTime.now();
        DatiCreazioneHackathon dati = new DatiCreazioneHackathon(
                "h-valid",
                "Hack Senza Mentori",
                "Regolamento",
                ora.plusDays(5),
                ora.plusDays(10),
                ora.plusDays(12),
                "Online",
                1000.0,
                4
        );

        assertThrows(IllegalArgumentException.class, () ->
                controller.creaHackathon(dati, organizzatore, giudice, List.of()));
    }
}
