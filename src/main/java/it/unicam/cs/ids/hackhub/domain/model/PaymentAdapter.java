package it.unicam.cs.ids.hackhub.domain.model;

import java.util.Objects;

/**
 * Adapter Pattern - Concrete Adapter:
 * Adatta l'interfaccia PaymentGateway al servizio esterno SistemaDiPagamentoEsterno.
 */
public class PaymentAdapter implements PaymentGateway {

    private final SistemaDiPagamentoEsterno servizioEsterno;

    public PaymentAdapter(SistemaDiPagamentoEsterno servizioEsterno) {
        this.servizioEsterno = Objects.requireNonNull(servizioEsterno, "Il servizio di pagamento esterno non può essere nullo");
    }

    @Override
    public boolean erogaPremio(String teamId, double importo) {
        if (teamId == null || importo <= 0.0) {
            return false;
        }
        SistemaDiPagamentoEsterno.RicevutaTransazione ricevuta =
                servizioEsterno.inviaPagamento("CONTO-TEAM-" + teamId, importo);
        return ricevuta.successo();
    }
}
