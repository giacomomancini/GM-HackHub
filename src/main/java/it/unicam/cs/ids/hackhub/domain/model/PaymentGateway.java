package it.unicam.cs.ids.hackhub.domain.model;

/**
 * Adapter Pattern - Interfaccia Target:
 * Definisce il contratto interno della piattaforma per l'erogazione dei pagamenti.
 */
public interface PaymentGateway {
    boolean erogaPremio(String teamId, double importo);
}
