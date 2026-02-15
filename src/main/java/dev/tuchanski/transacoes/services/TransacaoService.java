package dev.tuchanski.transacoes.services;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.exceptions.InvalidDataHoraException;
import dev.tuchanski.transacoes.exceptions.InvalidValorException;
import dev.tuchanski.transacoes.mappers.TransacaoMapper;
import dev.tuchanski.transacoes.models.Transacao;
import dev.tuchanski.transacoes.storage.TransacaoStorage;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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

}
