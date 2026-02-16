package dev.tuchanski.transacoes.services;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;

import java.util.HashMap;

public interface TransacaoService {
    void createTransacao(TransacaoRequestDTO dto);
    void clearTransacoes();
    HashMap<String, Number> getStatsUltimoMinuto();
}
