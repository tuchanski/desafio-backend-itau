package dev.tuchanski.transacoes.storage;

import dev.tuchanski.transacoes.models.Transacao;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransacaoStorage {

    private static List<Transacao> transacoes = new ArrayList<Transacao>();

    public static void addTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public static void clearTransacoes() {
        transacoes.clear();
    }

    public static List<Transacao> retrieveUltimoMinuto() {
        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime umMinAtras = agora.minusMinutes(1);

        return transacoes.stream()
                .filter(e -> e.getDataHora().isAfter(umMinAtras) &&
                        e.getDataHora().isBefore(agora.plusSeconds(1)))
                .toList();
    }

}
