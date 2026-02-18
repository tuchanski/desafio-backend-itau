package dev.tuchanski.transacoes.services.impl;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.exceptions.InvalidBodyRequestException;
import dev.tuchanski.transacoes.exceptions.InvalidDataHoraException;
import dev.tuchanski.transacoes.exceptions.InvalidValorException;
import dev.tuchanski.transacoes.mappers.TransacaoMapper;
import dev.tuchanski.transacoes.services.TransacaoService;
import dev.tuchanski.transacoes.storage.TransacaoStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoServiceImplTest {

    private TransacaoStorage storage;
    private TransacaoMapper mapper;
    private TransacaoServiceImpl service;

    @BeforeEach
    void setup() {
        this.storage = new TransacaoStorage();
        this.mapper = new TransacaoMapper();
        this.service = new TransacaoServiceImpl(mapper, storage);
    }

    @Test
    @DisplayName("Deve criar transação com sucesso")
    void deveCriarTransacaoComSucesso() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(60.90F, OffsetDateTime.now());
        service.createTransacao(dto);

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(1L, stats.get("count"));
    }

    @Test
    @DisplayName("Deve limpar as transações em memória")
    void deveLimparAsTransacoesEmMemoria() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(60.90F, OffsetDateTime.now());
        service.createTransacao(dto);

        TransacaoRequestDTO dto2 = new TransacaoRequestDTO(50.90F, OffsetDateTime.now());
        service.createTransacao(dto2);

        TransacaoRequestDTO dto3 = new TransacaoRequestDTO(40.90F, OffsetDateTime.now());
        service.createTransacao(dto3);

        service.clearTransacoes();

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(0, stats.get("count"));
    }

    @Test
    @DisplayName("Deve retornar corretamente a contagem de transações adicionadas no último minuto ")
    void deveRetornarCorretamenteContagemDeTransacoesAdicionadasNoUltimoMin() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(60.90F, OffsetDateTime.now().minusSeconds(10));
        service.createTransacao(dto);

        TransacaoRequestDTO dto2 = new TransacaoRequestDTO(50.90F, OffsetDateTime.now());
        service.createTransacao(dto2);

        TransacaoRequestDTO dto3 = new TransacaoRequestDTO(40.90F, OffsetDateTime.now().minusSeconds(20));
        service.createTransacao(dto3);

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(3L, stats.get("count"));
    }

    @Test
    @DisplayName("Não deve adicionar DTO com campo null")
    void naoDeveAdicionarTransacaoCampoNull() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(null, OffsetDateTime.now());
        assertThrows(InvalidBodyRequestException.class, () -> service.createTransacao(dto));
    }

    @Test
    @DisplayName("Não deve adicionar DTO com dataHora no futuro")
    void naoDeveAdicionarTransacaoNoFuturo() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(50.60F, OffsetDateTime.now().plusSeconds(50));
        assertThrows(InvalidDataHoraException.class, () -> service.createTransacao(dto));
    }

    @Test
    @DisplayName("Não deve adicionar DTO com valor negativo")
    void naoDeveAdicionarTransacaoComValorNegativo() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(-20F, OffsetDateTime.now());
        assertThrows(InvalidValorException.class, () -> service.createTransacao(dto));
    }

    @Test
    @DisplayName("Deve retornar média de preço correta nos stats")
    void deveRetornarMediaDePrecoCorretaNosStatsDoUltimoMinuto() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(60.90F, OffsetDateTime.now().minusSeconds(10));
        service.createTransacao(dto);

        TransacaoRequestDTO dto2 = new TransacaoRequestDTO(50.90F, OffsetDateTime.now());
        service.createTransacao(dto2);

        TransacaoRequestDTO dto3 = new TransacaoRequestDTO(40.90F, OffsetDateTime.now().minusSeconds(20));
        service.createTransacao(dto3);

        HashMap<String, Number> stats = service.getStats(1);

        float avg = (dto.valor() + dto2.valor() + dto3.valor()) / 3;

        assertEquals(String.format("%.2f", avg), String.format("%.2f", stats.get("avg")));
    }

    @Test
    @DisplayName("Borda: transação exatamente 60s atrás (defina se entra ou não)")
    void bordaExatamente60s() {
        OffsetDateTime t = OffsetDateTime.now().minusSeconds(60);
        service.createTransacao(new TransacaoRequestDTO(10F, t));

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(0, stats.get("count"));
    }

    @Test
    @DisplayName("Borda: transação 61s atrás não entra")
    void borda61sNaoEntra() {
        service.createTransacao(new TransacaoRequestDTO(10F, OffsetDateTime.now().minusSeconds(61)));

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(0, stats.get("count"));
    }

    @Test
    @DisplayName("Stats vazio não explode e retorna zeros/valores esperados")
    void statsVazio() {
        service.clearTransacoes();

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(0, stats.get("count"));
        assertEquals(0F, stats.get("sum").floatValue());
        assertEquals(0F, stats.get("avg").floatValue());
        assertEquals(0F, stats.get("min").floatValue());
        assertEquals(0F, stats.get("max").floatValue());
    }

    @Test
    @DisplayName("Stats retorna min e max corretos no último minuto")
    void statsMinMax() {
        service.createTransacao(new TransacaoRequestDTO(60.90F, OffsetDateTime.now().minusSeconds(10)));
        service.createTransacao(new TransacaoRequestDTO(50.90F, OffsetDateTime.now()));
        service.createTransacao(new TransacaoRequestDTO(40.90F, OffsetDateTime.now().minusSeconds(20)));

        HashMap<String, Number> stats = service.getStats(1);

        assertEquals(40.90F, stats.get("min").floatValue(), 0.0001);
        assertEquals(60.90F, stats.get("max").floatValue(), 0.0001);
    }

    @Test
    @DisplayName("Clear é idempotente")
    void clearIdempotente() {
        service.clearTransacoes();
        service.clearTransacoes();

        assertEquals(0, service.getStats(1).get("count"));
    }
}