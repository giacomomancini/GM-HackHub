package it.unicam.cs.ids.hackhub.domain.model;

/**
 * Adapter Pattern - Adaptee:
 * Simula il servizio esterno proprietario di pagamento.
 */
public class SistemaDiPagamentoEsterno {

    public record RicevutaTransazione(boolean successo, String codiceAutorizzazione) {}

    public RicevutaTransazione inviaPagamento(String contoDestinatario, double somma) {
        if (contoDestinatario == null || contoDestinatario.isBlank() || somma <= 0.0) {
            return new RicevutaTransazione(false, null);
        }
        return new RicevutaTransazione(true, "PAY-AUTH-" + System.currentTimeMillis());
    }
}
