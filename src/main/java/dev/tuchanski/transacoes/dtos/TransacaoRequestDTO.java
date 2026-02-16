package dev.tuchanski.transacoes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record TransacaoRequestDTO(
        @Schema(description = "Valor da transação", example = "150.50") Float valor,
        @Schema(description = "Timestamp da transação", example = "2022-02-16T09:58:30.789-03:00") OffsetDateTime dataHora) {
}
