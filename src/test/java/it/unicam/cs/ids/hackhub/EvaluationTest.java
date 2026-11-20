package it.unicam.cs.ids.hackhub;

import it.unicam.cs.ids.hackhub.controller.EvaluationController;
import it.unicam.cs.ids.hackhub.domain.model.*;
import it.unicam.cs.ids.hackhub.domain.state.InIscrizioneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTest {

    private EvaluationController controller;
    private Hackathon hackathon;
    private Giudice giudiceAssegnato;
    private Giudice altroGiudice;
    private Sottomissione sottomissione;

    @BeforeEach
    void setUp() {
        controller = new EvaluationController();
        Organizzatore org = new Organizzatore("org-1", "Mario Rossi", "mario@unicam.it", "pwd");
        giudiceAssegnato = new Giudice("giu-1", "Luigi Bianchi", "luigi@unicam.it", "pwd");
        altroGiudice = new Giudice("giu-99", "Impostore", "fake@test.it", "pwd");
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
                giudiceAssegnato,
                mentori,
                new InIscrizioneState()
        );

        sottomissione = new Sottomissione(
                "sub-1",
                "team-1",
                "Progetto Smart City",
                "Soluzione IoT per la mobilità urbana",
                "https://github.com/team1/smart-city"
        );
        hackathon.aggiungiSottomissione(sottomissione);
    }

    @Test
    @DisplayName("UC-G1 Ramo Eccezione: Errore se hackathon non è in stato IN_VALUTAZIONE")
    void testErroreSeNonInValutazione() {
        assertThrows(OperazioneNonConsentitaException.class, () ->
                controller.valuta(hackathon, giudiceAssegnato, sottomissione.getId(), 8.5, "Ottimo lavoro!")
        );
    }

    @Test
    @DisplayName("UC-G1 Ramo Eccezione: Errore se il giudice non è quello assegnato")
    void testErroreGiudiceNonAutorizzato() {
        hackathon.avanzaAInValutazione();

        assertThrows(OperazioneNonConsentitaException.class, () ->
                controller.valuta(hackathon, altroGiudice, sottomissione.getId(), 8.5, "Ottimo lavoro!")
        );
    }

    @Test
    @DisplayName("UC-G1 Ramo Eccezione: Errore punteggio fuori intervallo (< 0 o > 10)")
    void testErrorePunteggioFuoriRange() {
        hackathon.avanzaAInValutazione();

        assertThrows(IllegalArgumentException.class, () ->
                controller.valuta(hackathon, giudiceAssegnato, sottomissione.getId(), 10.5, "Troppo alto!")
        );
        assertThrows(IllegalArgumentException.class, () ->
                controller.valuta(hackathon, giudiceAssegnato, sottomissione.getId(), -1.0, "Negativo!")
        );
    }

    @Test
    @DisplayName("UC-G1 Flusso Principale: Valutazione registrata con successo")
    void testValutazioneSuccesso() {
        hackathon.avanzaAInValutazione();

        Valutazione v = controller.valuta(hackathon, giudiceAssegnato, sottomissione.getId(), 9.0, "Progetto eccellente e innovativo.");

        assertNotNull(v);
        assertEquals(9.0, v.getPunteggio());
        assertEquals("Progetto eccellente e innovativo.", v.getGiudizio());
        assertEquals(giudiceAssegnato, v.getGiudice());
        assertTrue(sottomissione.isValutata());
        assertTrue(hackathon.tutteSottomissioniValutate());
    }
}
