package dev.tuchanski.transacoes.services;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.exceptions.InvalidDataHoraException;
import dev.tuchanski.transacoes.exceptions.InvalidValorException;
import dev.tuchanski.transacoes.mappers.TransacaoMapper;
import dev.tuchanski.transacoes.models.Transacao;
import dev.tuchanski.transacoes.storage.TransacaoStorage;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoMapper mapper;

    public TransacaoService(TransacaoMapper mapper) {
        this.mapper = mapper;
    }

    public void createTransacao(TransacaoRequestDTO dto) {
        OffsetDateTime now = OffsetDateTime.now();

        if (dto.dataHora().isAfter(now)) {
            throw new InvalidDataHoraException("Não dá pra criar uma transação no futuro.");
        }

        if (dto.valor() < 0) {
            throw new InvalidValorException("Não dá pra criar uma transação com valor negativo.");
        }

        Transacao transacao = mapper.toEntity(dto);
        TransacaoStorage.addTransacao(transacao);
    }

    public void clearTransacoes() {
        TransacaoStorage.clearTransacoes();
    }

    public HashMap<String, Number> getStatsUltimoMinuto() {
        List<Transacao> lastTransacoes = TransacaoStorage.retrieveUltimoMinuto();
        HashMap<String, Number> stats = new HashMap<>();

        if (lastTransacoes.isEmpty()) {
            stats.put("count", 0);
            stats.put("sum", 0);
            stats.put("avg", 0);
            stats.put("min", 0);
            stats.put("max", 0);

            return stats;
        }

        int count = 0;
        float sum = 0;
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        for (Transacao t : lastTransacoes) {
            count++;
            sum += t.getValor();
            min = Math.min(min, t.getValor());
            max = Math.max(max, t.getValor());
        }

        double avg = sum / count;

        stats.put("count", count);
        stats.put("sum", sum);
        stats.put("avg", avg);
        stats.put("min", min);
        stats.put("max", max);

        return stats;
    }


}
