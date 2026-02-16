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

    public List<Transacao> retrieveUltimoMinuto() {
        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime umMinAtras = agora.minusMinutes(1);

        return transacoes.stream()
                .filter(e -> !e.getDataHora().isBefore(umMinAtras) &&
                        !e.getDataHora().isAfter(agora))
                .toList();
    }

}
