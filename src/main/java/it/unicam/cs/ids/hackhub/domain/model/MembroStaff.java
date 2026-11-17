package it.unicam.cs.ids.hackhub.domain.model;

/**
 * Membro dello Staff assegnabile a un hackathon.
 */
public abstract class MembroStaff extends Utente {
    private final String ruolo;

    public MembroStaff(String id, String nome, String email, String password, String ruolo) {
        super(id, nome, email, password);
        this.ruolo = ruolo;
    }

    public String getRuolo() {
        return ruolo;
    }
}
