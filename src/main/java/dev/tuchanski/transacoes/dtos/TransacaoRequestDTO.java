package dev.tuchanski.transacoes.dtos;

import java.time.OffsetDateTime;

public record TransacaoRequestDTO(float valor, OffsetDateTime dataHora) {
}
