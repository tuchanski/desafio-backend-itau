package dev.tuchanski.transacoes.services;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.mappers.TransacaoMapper;
import dev.tuchanski.transacoes.models.Transacao;
import dev.tuchanski.transacoes.storage.TransacaoStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoMapper mapper;

    public TransacaoService(TransacaoMapper mapper) {
        this.mapper = mapper;
    }

    public void createTransacao(TransacaoRequestDTO dto) {
        Transacao transacao = mapper.toEntity(dto);
        TransacaoStorage.addTransacao(transacao);
    }

}
