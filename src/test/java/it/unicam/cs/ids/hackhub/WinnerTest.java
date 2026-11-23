package it.unicam.cs.ids.hackhub;

import it.unicam.cs.ids.hackhub.controller.WinnerController;
import it.unicam.cs.ids.hackhub.domain.model.*;
import it.unicam.cs.ids.hackhub.domain.state.InIscrizioneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WinnerTest {

    private WinnerController controller;
    private Hackathon hackathon;
    private Team team1;
    private Team team2;
    private Sottomissione sub1;
    private Sottomissione sub2;
    private Giudice giudice;

    @BeforeEach
    void setUp() {
        SistemaDiPagamentoEsterno servizioEsterno = new SistemaDiPagamentoEsterno();
        PaymentGateway adapter = new PaymentAdapter(servizioEsterno);
        controller = new WinnerController(adapter);

        Organizzatore org = new Organizzatore("org-1", "Mario Rossi", "mario@unicam.it", "pwd");
        giudice = new Giudice("giu-1", "Luigi Bianchi", "luigi@unicam.it", "pwd");
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
                2000.0,
                5,
                org,
                giudice,
                mentori,
                new InIscrizioneState()
        );

        Utente u1 = new Utente("u-1", "Alice", "alice@test.it", "pwd");
        Utente u2 = new Utente("u-2", "Bob", "bob@test.it", "pwd");
        team1 = new Team("t-1", "Alpha Team", u1);
        team2 = new Team("t-2", "Beta Team", u2);

        sub1 = new Sottomissione("s-1", team1.getId(), "App Alpha", "Desc Alpha", "https://github.com/alpha");
        sub2 = new Sottomissione("s-2", team2.getId(), "App Beta", "Desc Beta", "https://github.com/beta");

        hackathon.aggiungiSottomissione(sub1);
        hackathon.aggiungiSottomissione(sub2);
    }

    @Test
    @DisplayName("UC-O4 Ramo Eccezione 1.2: Errore se non tutte le sottomissioni sono valutate")
    void testErroreSeNonTutteValutate() {
        hackathon.avanzaAInValutazione();

        // Valutiamo solo sub1, sub2 resta non valutata
        sub1.assegnaValutazione(new Valutazione(8.0, "Buono", giudice));

        assertThrows(OperazioneNonConsentitaException.class, () ->
                controller.proclamaVincitore(hackathon, team1.getId())
        );
        assertFalse(hackathon.isConcluso());
    }

    @Test
    @DisplayName("UC-O4 Flusso Principale 1.3-1.10: Proclamazione vincitore ed erogazione premio con successo")
    void testProclamazioneVincitoreSuccesso() {
        hackathon.avanzaAInValutazione();

        // Valutiamo entrambe le sottomissioni
        sub1.assegnaValutazione(new Valutazione(9.5, "Eccezionale", giudice));
        sub2.assegnaValutazione(new Valutazione(7.0, "Discreto", giudice));

        boolean esito = controller.proclamaVincitore(hackathon, team1.getId());

        assertTrue(esito);
        assertEquals(team1.getId(), hackathon.getIdTeamVincitore());
        assertTrue(hackathon.isConcluso());
        assertEquals("CONCLUSO", hackathon.getStato().getNomeStato());
    }

    @Test
    @DisplayName("State Pattern: Nessuna operazione permessa quando l'hackathon è CONCLUSO")
    void testNessunaOperazionePermessaSuConcluso() {
        hackathon.avanzaAInValutazione();
        sub1.assegnaValutazione(new Valutazione(9.5, "Eccezionale", giudice));
        sub2.assegnaValutazione(new Valutazione(7.0, "Discreto", giudice));
        controller.proclamaVincitore(hackathon, team1.getId());

        // Verifichiamo che i metodi di modifica lanciano eccezione
        assertThrows(OperazioneNonConsentitaException.class, () ->
                hackathon.getStato().iscriviTeam(hackathon, team1)
        );
        assertThrows(OperazioneNonConsentitaException.class, () ->
                hackathon.getStato().inviaSottomissione(hackathon, sub1)
        );
        assertThrows(OperazioneNonConsentitaException.class, () ->
                hackathon.getStato().valuta(hackathon)
        );
    }
}
