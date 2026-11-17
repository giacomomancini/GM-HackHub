package it.unicam.cs.ids.hackhub.domain.model;

public class Organizzatore extends MembroStaff {
    public Organizzatore(String id, String nome, String email, String password) {
        super(id, nome, email, password, "ORGANIZZATORE");
    }
}
