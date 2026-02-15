package dev.tuchanski.transacoes.storage;

import dev.tuchanski.transacoes.models.Transacao;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransacaoStorage {

    private static List<Transacao> transacoes = new ArrayList<Transacao>();

    public static void addTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public static void clear() {
        transacoes.clear();
    }

    public static List<Transacao> retrieveUltimoMinuto() {
        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime umMinAtras = agora.minus(1, ChronoUnit.MINUTES);

        return transacoes.stream()
                .filter(e -> e.getDataHora().isAfter(umMinAtras) &&
                        e.getDataHora().isBefore(agora.plusSeconds(1)))
                .toList();
    }

}
