package dev.tuchanski.transacoes.services;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.exceptions.InvalidBodyRequestException;
import dev.tuchanski.transacoes.exceptions.InvalidDataHoraException;
import dev.tuchanski.transacoes.exceptions.InvalidValorException;
import dev.tuchanski.transacoes.mappers.TransacaoMapper;
import dev.tuchanski.transacoes.models.Transacao;
import dev.tuchanski.transacoes.storage.TransacaoStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
@AllArgsConstructor
public class TransacaoService {

    @Autowired
    private final TransacaoMapper mapper;

    @Autowired
    private final TransacaoStorage storage;

    public void createTransacao(TransacaoRequestDTO dto) {

        if (dto.dataHora() == null || dto.valor() == null) {
            throw new InvalidBodyRequestException("Não dá pra criar uma transação com corpo da requisição inválido.");
        }

        OffsetDateTime now = OffsetDateTime.now();

        if (dto.dataHora().isAfter(now)) {
            throw new InvalidDataHoraException("Não dá pra criar uma transação no futuro.");
        }

        if (dto.valor() < 0) {
            throw new InvalidValorException("Não dá pra criar uma transação com valor negativo.");
        }

        Transacao transacao = mapper.toEntity(dto);
        storage.addTransacao(transacao);
    }

    public void clearTransacoes() {
        storage.clearTransacoes();
    }

    public HashMap<String, Number> getStatsUltimoMinuto() {
        List<Transacao> lastTransacoes = storage.retrieveUltimoMinuto();
        DoubleSummaryStatistics stats = lastTransacoes.stream().collect(Collectors.summarizingDouble(Transacao::getValor));

        if (stats.getCount() == 0) {
            return new HashMap<>(Map.of(
                    "count", 0,
                    "sum", stats.getSum(),
                    "avg", stats.getAverage(),
                    "min", 0,
                    "max", 0
            ));
        }

        return new HashMap<>(Map.of(
                "count", stats.getCount(),
                "sum", stats.getSum(),
                "avg", stats.getAverage(),
                "min", stats.getMin(),
                "max", stats.getMax()
        ));
    }

}
