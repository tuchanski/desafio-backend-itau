package dev.tuchanski.transacoes.dtos;

import java.time.OffsetDateTime;

public record TransacaoRequestDTO(Float valor, OffsetDateTime dataHora) {
}
