package it.unicam.cs.ids.hackhub.controller;

import it.unicam.cs.ids.hackhub.domain.model.Hackathon;
import it.unicam.cs.ids.hackhub.domain.model.OperazioneNonConsentitaException;
import it.unicam.cs.ids.hackhub.domain.model.PaymentGateway;
import it.unicam.cs.ids.hackhub.domain.state.ConclusoState;

import java.util.Objects;

/**
 * Controller per la realizzazione del caso d'uso UC-O4: Proclamazione Vincitore ed Erogazione Premio.
 */
public class WinnerController {

    private final PaymentGateway paymentGateway;

    public WinnerController(PaymentGateway paymentGateway) {
        this.paymentGateway = Objects.requireNonNull(paymentGateway, "Il gateway di pagamento non può essere nullo");
    }

    public boolean proclamaVincitore(Hackathon hackathon, String idTeamVincitore) {
        Objects.requireNonNull(hackathon, "L'hackathon non può essere nullo");
        Objects.requireNonNull(idTeamVincitore, "L'id del team vincitore non può essere nullo");

        if (!hackathon.tutteSottomissioniValutate()) {
            throw new OperazioneNonConsentitaException(
                    "Operazione non consentita: impossibile proclamare il vincitore prima che tutte le sottomissioni siano state valutate."
            );
        }

        double premio = hackathon.getPremio();
        boolean pagamentoRiuscito = paymentGateway.erogaPremio(idTeamVincitore, premio);

        if (!pagamentoRiuscito) {
            throw new OperazioneNonConsentitaException(
                    "Transazione di pagamento fallita: il premio non è stato erogato. L'hackathon rimane in stato di valutazione."
            );
        }

        hackathon.impostaVincitore(idTeamVincitore);
        ConclusoState statoFinale = new ConclusoState();
        hackathon.concludi(statoFinale);

        return true;
    }
}
