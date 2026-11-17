package it.unicam.cs.ids.hackhub.domain.model;

public class Giudice extends MembroStaff {
    public Giudice(String id, String nome, String email, String password) {
        super(id, nome, email, password, "GIUDICE");
    }
}
