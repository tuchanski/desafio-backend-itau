package dev.tuchanski.transacoes.storage;

import dev.tuchanski.transacoes.models.Transacao;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransacaoStorage {

    private static List<Transacao> transacoes = new ArrayList<Transacao>();
    private static final Logger logger = LoggerFactory.getLogger(TransacaoStorage.class);

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
