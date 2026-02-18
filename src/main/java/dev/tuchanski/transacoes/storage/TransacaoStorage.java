package dev.tuchanski.transacoes.storage;

import dev.tuchanski.transacoes.models.Transacao;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransacaoStorage {

    private final List<Transacao> transacoes = new ArrayList<Transacao>();

    public void addTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public void clearTransacoes() {
        transacoes.clear();
    }

    public List<Transacao> retrieveStats(int tempoEmMinutos) {
        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime intervaloInicio = agora.minusMinutes(tempoEmMinutos);

        return transacoes.stream()
                .filter(e -> !e.getDataHora().isBefore(intervaloInicio) &&
                        !e.getDataHora().isAfter(agora))
                .toList();
    }

}
