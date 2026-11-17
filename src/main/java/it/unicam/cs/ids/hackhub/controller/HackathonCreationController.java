package it.unicam.cs.ids.hackhub.controller;

import it.unicam.cs.ids.hackhub.domain.model.*;
import it.unicam.cs.ids.hackhub.domain.state.InIscrizioneState;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller per la realizzazione del caso d'uso UC-O1: Creazione Hackathon.
 */
public class HackathonCreationController {

    private final Map<String, Hackathon> repositoryHackathon = new HashMap<>();

    public record DatiCreazioneHackathon(
            String id,
            String nome,
            String regolamento,
            LocalDateTime scadenzaIscrizioni,
            LocalDateTime dataInizio,
            LocalDateTime dataFine,
            String luogo,
            double premio,
            int dimMaxTeam
    ) {}

    public Hackathon creaHackathon(DatiCreazioneHackathon dati,
                                  Organizzatore organizzatore,
                                  Giudice giudice,
                                  List<Mentore> mentori) {
        validaDatiEDate(dati);
        validaStaff(giudice, mentori);

        InIscrizioneState statoIniziale = new InIscrizioneState();

        Hackathon nuovoHackathon = new Hackathon(
                dati.id(),
                dati.nome(),
                dati.regolamento(),
                dati.scadenzaIscrizioni(),
                dati.dataInizio(),
                dati.dataFine(),
                dati.luogo(),
                dati.premio(),
                dati.dimMaxTeam(),
                organizzatore,
                giudice,
                mentori,
                statoIniziale
        );

        salvaHackathon(nuovoHackathon);
        return nuovoHackathon;
    }

    public void validaDatiEDate(DatiCreazioneHackathon dati) {
        if (dati == null) {
            throw new IllegalArgumentException("I dati dell'evento sono obbligatori.");
        }
        if (dati.premio() <= 0) {
            throw new IllegalArgumentException("Il premio deve essere strettamente positivo.");
        }
        if (dati.dimMaxTeam() < 1) {
            throw new IllegalArgumentException("La dimensione del team deve essere almeno 1.");
        }
        if (dati.scadenzaIscrizioni().isAfter(dati.dataInizio())) {
            throw new IllegalArgumentException("La scadenza iscrizioni non può essere successiva alla data di inizio.");
        }
        if (dati.dataInizio().isAfter(dati.dataFine())) {
            throw new IllegalArgumentException("La data di inizio non può essere successiva alla data di fine.");
        }
    }

    public void validaStaff(Giudice giudice, List<Mentore> mentori) {
        if (giudice == null) {
            throw new IllegalArgumentException("È obbligatorio assegnare esattamente un giudice.");
        }
        if (mentori == null || mentori.isEmpty()) {
            throw new IllegalArgumentException("È obbligatorio assegnare almeno un mentore all'hackathon.");
        }
    }

    public void salvaHackathon(Hackathon hackathon) {
        repositoryHackathon.put(hackathon.getId(), hackathon);
    }

    public Optional<Hackathon> trovaPerId(String id) {
        return Optional.ofNullable(repositoryHackathon.get(id));
    }
}
