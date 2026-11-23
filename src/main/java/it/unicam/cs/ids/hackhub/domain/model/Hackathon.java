package it.unicam.cs.ids.hackhub.domain.model;

import it.unicam.cs.ids.hackhub.domain.state.ConclusoState;
import it.unicam.cs.ids.hackhub.domain.state.HackathonState;
import it.unicam.cs.ids.hackhub.domain.state.InCorsoState;
import it.unicam.cs.ids.hackhub.domain.state.InIscrizioneState;
import it.unicam.cs.ids.hackhub.domain.state.InValutazioneState;

import java.time.LocalDateTime;
import java.util.*;

public class Hackathon {
    private final String id;
    private final String nome;
    private final String regolamento;
    private final LocalDateTime scadenzaIscrizioni;
    private final LocalDateTime dataInizio;
    private final LocalDateTime dataFine;
    private final String luogo;
    private final double premio;
    private final int dimMaxTeam;

    private final Organizzatore organizzatore;
    private final Giudice giudice;
    private final List<Mentore> mentori = new ArrayList<>();

    private final List<Team> teamIscritti = new ArrayList<>();
    private final List<Sottomissione> sottomissioni = new ArrayList<>();
    private final List<RichiestaSupporto> richiesteSupporto = new ArrayList<>();

    private HackathonState stato;
    private String idTeamVincitore;

    public Hackathon(String id,
                     String nome,
                     String regolamento,
                     LocalDateTime scadenzaIscrizioni,
                     LocalDateTime dataInizio,
                     LocalDateTime dataFine,
                     String luogo,
                     double premio,
                     int dimMaxTeam,
                     Organizzatore organizzatore,
                     Giudice giudice,
                     List<Mentore> mentori,
                     HackathonState statoIniziale) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.regolamento = Objects.requireNonNull(regolamento);
        this.scadenzaIscrizioni = Objects.requireNonNull(scadenzaIscrizioni);
        this.dataInizio = Objects.requireNonNull(dataInizio);
        this.dataFine = Objects.requireNonNull(dataFine);
        this.luogo = Objects.requireNonNull(luogo);
        this.premio = premio;
        this.dimMaxTeam = dimMaxTeam;
        this.organizzatore = Objects.requireNonNull(organizzatore);
        this.giudice = Objects.requireNonNull(giudice);
        if (mentori == null || mentori.isEmpty()) {
            throw new IllegalArgumentException("È richiesto almeno un mentore.");
        }
        this.mentori.addAll(mentori);
        this.stato = (statoIniziale != null) ? statoIniziale : new InIscrizioneState();
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getRegolamento() { return regolamento; }
    public LocalDateTime getScadenzaIscrizioni() { return scadenzaIscrizioni; }
    public LocalDateTime getDataInizio() { return dataInizio; }
    public LocalDateTime getDataFine() { return dataFine; }
    public String getLuogo() { return luogo; }
    public double getPremio() { return premio; }
    public int getDimMaxTeam() { return dimMaxTeam; }
    public Organizzatore getOrganizzatore() { return organizzatore; }
    public Giudice getGiudice() { return giudice; }
    public List<Mentore> getMentori() { return Collections.unmodifiableList(mentori); }
    public List<Team> getTeamIscritti() { return Collections.unmodifiableList(teamIscritti); }
    public List<Sottomissione> getSottomissioni() { return Collections.unmodifiableList(sottomissioni); }
    public List<RichiestaSupporto> getRichiesteSupporto() { return Collections.unmodifiableList(richiesteSupporto); }
    public HackathonState getStato() { return stato; }
    public String getIdTeamVincitore() { return idTeamVincitore; }

    public void setStato(HackathonState stato) {
        this.stato = stato;
    }

    public void avanzaAInCorso() {
        this.stato = new InCorsoState();
    }

    public void avanzaAInValutazione() {
        this.stato = new InValutazioneState();
    }

    public void impostaVincitore(String idTeamVincitore) {
        this.idTeamVincitore = Objects.requireNonNull(idTeamVincitore, "L'id del team vincitore non può essere nullo");
    }

    public void concludi(HackathonState nuovoStato) {
        this.stato = Objects.requireNonNull(nuovoStato);
    }

    public boolean isConcluso() {
        return "CONCLUSO".equals(this.stato.getNomeStato());
    }

    public boolean isInvioAperto() {
        if (!"IN_CORSO".equals(this.stato.getNomeStato())) {
            return false;
        }
        return !LocalDateTime.now().isAfter(dataFine);
    }

    public boolean isValutazioneAperta(String idGiudice) {
        if (!"IN_VALUTAZIONE".equals(this.stato.getNomeStato())) {
            return false;
        }
        return this.giudice.getId().equals(idGiudice);
    }

    /**
     * Verifica se il supporto dei mentori è aperto.
     * È aperto solo se lo stato è IN_CORSO e il mentore fa parte dello staff assegnato.
     */
    public boolean isSupportoAperto(String idMentore) {
        if (!"IN_CORSO".equals(this.stato.getNomeStato())) {
            return false;
        }
        for (Mentore m : mentori) {
            if (m.getId().equals(idMentore)) {
                return true;
            }
        }
        return false;
    }

    public Sottomissione getSottomissione(String teamId) {
        for (Sottomissione s : sottomissioni) {
            if (s.getTeamId().equals(teamId)) {
                return s;
            }
        }
        return null;
    }

    public Sottomissione trovaSottomissionePerId(String idSottomissione) {
        for (Sottomissione s : sottomissioni) {
            if (s.getId().equals(idSottomissione)) {
                return s;
            }
        }
        return null;
    }

    public RichiestaSupporto trovaRichiestaSupportoPerId(String idRichiesta) {
        for (RichiestaSupporto r : richiesteSupporto) {
            if (r.getId().equals(idRichiesta)) {
                return r;
            }
        }
        return null;
    }

    public boolean tutteSottomissioniValutate() {
        if (sottomissioni.isEmpty()) return false;
        for (Sottomissione s : sottomissioni) {
            if (!s.isValutata()) return false;
        }
        return true;
    }

    public void aggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(Objects.requireNonNull(sottomissione));
    }

    public void aggiungiRichiestaSupporto(RichiestaSupporto richiesta) {
        this.richiesteSupporto.add(Objects.requireNonNull(richiesta));
    }

    public void aggiungiTeamIscritto(Team team) {
        if (!teamIscritti.contains(team)) {
            teamIscritti.add(team);
        }
    }

    public void aggiungiMentore(Mentore mentore) {
        if (!mentori.contains(mentore)) {
            mentori.add(mentore);
        }
    }
}
