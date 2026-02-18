package dev.tuchanski.transacoes.services.impl;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.exceptions.InvalidBodyRequestException;
import dev.tuchanski.transacoes.exceptions.InvalidDataHoraException;
import dev.tuchanski.transacoes.exceptions.InvalidPeriodoDeTempoException;
import dev.tuchanski.transacoes.exceptions.InvalidValorException;
import dev.tuchanski.transacoes.mappers.TransacaoMapper;
import dev.tuchanski.transacoes.models.Transacao;
import dev.tuchanski.transacoes.services.TransacaoService;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class TransacaoServiceImpl implements TransacaoService {

    private final Logger logger = LoggerFactory.getLogger(TransacaoServiceImpl.class);

    @Autowired
    private final TransacaoMapper mapper;

    @Autowired
    private final TransacaoStorage storage;

    public void createTransacao(TransacaoRequestDTO dto) {
        logger.info("INFO: Requisição de Transação recebida.");

        if (dto.dataHora() == null || dto.valor() == null) {
            logger.error("ERROR: Corpo da requisição inválido.");
            throw new InvalidBodyRequestException("Não dá pra criar uma transação com corpo da requisição inválido.");
        }

        OffsetDateTime now = OffsetDateTime.now();

        if (dto.dataHora().isAfter(now)) {
            logger.error("ERROR: Transação no futuro.");
            throw new InvalidDataHoraException("Não dá pra criar uma transação no futuro.");
        }

        if (dto.valor() < 0) {
            logger.error("ERROR: Valor negativo.");
            throw new InvalidValorException("Não dá pra criar uma transação com valor negativo.");
        }

        Transacao transacao = mapper.toEntity(dto);

        storage.addTransacao(transacao);
        logger.info("SUCCESS: Transação registrada!");
    }

    public void clearTransacoes() {
        logger.info("INFO: Limpando histórico de transações.");
        storage.clearTransacoes();
    }

    public HashMap<String, Number> getStats(int tempoEmMinutos) {
        logger.info("INFO: Requisição de Stats recebida.");

        if (tempoEmMinutos <= 0) {
            throw new InvalidPeriodoDeTempoException("O tempo informado deve ser maior que 0.");
        }

        logger.info("Recuperando stats em minutos: {}", tempoEmMinutos);
        List<Transacao> lastTransacoes = storage.retrieveStats(tempoEmMinutos);
        DoubleSummaryStatistics stats = lastTransacoes.stream().collect(Collectors.summarizingDouble(Transacao::getValor));

        if (stats.getCount() == 0) {
            logger.info("INFO: Sem transações registradas no intervalo.");
            return new HashMap<>(Map.of(
                    "count", 0,
                    "sum", 0,
                    "avg", 0,
                    "min", 0,
                    "max", 0
            ));
        }

        logger.info("INFO: Stats das transações efetuadas no intervalo recuperadas com sucesso.");
        return new HashMap<>(Map.of(
                "count", stats.getCount(),
                "sum", stats.getSum(),
                "avg", stats.getAverage(),
                "min", stats.getMin(),
                "max", stats.getMax()
        ));
    }

}
