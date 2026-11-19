package it.unicam.cs.ids.hackhub.domain.model;

/**
 * Eccezione sollevata quando un'operazione non è consentita
 * nello stato corrente dell'Hackathon.
 */
public class OperazioneNonConsentitaException extends RuntimeException {
    public OperazioneNonConsentitaException(String message) {
        super(message);
    }
}
