package dev.tuchanski.transacoes.mappers;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.models.Transacao;
import org.springframework.stereotype.Component;

@Component
public class TransacaoMapper {

    public Transacao toEntity(TransacaoRequestDTO dto) {
        Transacao transacao = new Transacao();

        transacao.setDataHora(dto.dataHora());
        transacao.setValor(dto.valor());

        return transacao;
    }

}
