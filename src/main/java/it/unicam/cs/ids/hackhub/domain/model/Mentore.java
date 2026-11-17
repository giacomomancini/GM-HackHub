package it.unicam.cs.ids.hackhub.domain.model;

public class Mentore extends MembroStaff {
    public Mentore(String id, String nome, String email, String password) {
        super(id, nome, email, password, "MENTORE");
    }
}
