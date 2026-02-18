package dev.tuchanski.transacoes.storage;

import dev.tuchanski.transacoes.models.Transacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransacaoStorageTest {

    private TransacaoStorage storage;

    @BeforeEach
    void setup() {
        storage = new TransacaoStorage();
    }

    @Test
    @DisplayName("Deve retornar transação ao adicionar transação válida")
    void deveAdicionarTransacao() {
        Transacao transacao = new Transacao(5.50F, OffsetDateTime.now());
        storage.addTransacao(transacao);
        assertFalse(storage.retrieveStats(1).isEmpty());
    }

    @Test
    @DisplayName("Deve limpar a lista de transações em memória")
    void deveLimparMemoriaQuandoClearForChamado() {
        Transacao transacao = new Transacao(5.50F, OffsetDateTime.now());
        storage.addTransacao(transacao);
        Transacao transacao2 = new Transacao(5.50F, OffsetDateTime.now());
        storage.addTransacao(transacao2);

        storage.clearTransacoes();

        assertTrue(storage.retrieveStats(1).isEmpty());
    }

    @Test
    @DisplayName("Deve recuperar apenas as transações do último minuto")
    void deveRecuperarApenasTransacoesDoUltimoMinuto() {
        Transacao transacao = new Transacao(5.50F, OffsetDateTime.now());
        storage.addTransacao(transacao);
        Transacao transacao2 = new Transacao(5.50F, OffsetDateTime.now().minusMinutes(20));
        storage.addTransacao(transacao2);

        assertEquals(1, storage.retrieveStats(1).size());
    }

}