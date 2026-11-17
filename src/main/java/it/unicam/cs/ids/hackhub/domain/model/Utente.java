package it.unicam.cs.ids.hackhub.domain.model;

import java.util.Objects;

/**
 * Rappresenta un utente registrato alla piattaforma HackHub.
 */
public class Utente {
    private final String id;
    private final String nome;
    private final String email;
    private String password;

    public Utente(String id, String nome, String email, String password) {
        this.id = Objects.requireNonNull(id, "L'id utente non può essere nullo");
        this.nome = Objects.requireNonNull(nome, "Il nome utente non può essere nullo");
        this.email = Objects.requireNonNull(email, "L'email non può essere nulla");
        this.password = Objects.requireNonNull(password, "La password non può essere nulla");
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public boolean verificaPassword(String passwordDaVerificare) {
        return this.password.equals(passwordDaVerificare);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utente utente)) return false;
        return Objects.equals(id, utente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
