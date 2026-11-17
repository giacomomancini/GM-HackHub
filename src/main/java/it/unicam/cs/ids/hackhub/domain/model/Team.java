package it.unicam.cs.ids.hackhub.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Team {
    private final String id;
    private final String nome;
    private final List<Utente> membri = new ArrayList<>();

    public Team(String id, String nome, Utente fondatore) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.membri.add(Objects.requireNonNull(fondatore));
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Utente> getMembri() {
        return Collections.unmodifiableList(membri);
    }

    public void aggiungiMembro(Utente utente) {
        if (!membri.contains(utente)) {
            membri.add(utente);
        }
    }

    public int getDimensione() {
        return membri.size();
    }
}
