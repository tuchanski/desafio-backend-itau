package dev.tuchanski.transacoes.controllers;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.services.impl.TransacaoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Tag(name = "Transações", description = "Endpoints para criação e análise de transações")
@RestController
@AllArgsConstructor
public class TransacaoController {

    @Autowired
    private final TransacaoServiceImpl service;

    @Operation(description = "Operação para criar uma transação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Transação não aceita por não atender 1 ou mais critérios de aceite"),
            @ApiResponse(responseCode = "400", description = "Corpo da requisição vazio")
    })
    @PostMapping(value = "/transacao", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Parameter(description = "Dados da transação a ser criada", required = true) @RequestBody TransacaoRequestDTO dto) {
        this.service.createTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(description = "Operação para limpar armazenamento em memória")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de transações limpa com sucesso")
    })
    @DeleteMapping("/transacao")
    public ResponseEntity<Void> deleteAll() {
        this.service.clearTransacoes();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(description = "Operação para recuperar estatísticas das transações criadas no intervalo desejado em minutos")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Estatísticas recuperadas com sucesso", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{ \"count\": 0, \"sum\": 0, \"min\": 0, \"avg\": 0, \"max\": 0 }"
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "Intervalo em minutos informado menor ou igual a 0")
            }
    )
    @GetMapping("/estatistica")
    public ResponseEntity<HashMap<String, Number>> getStats(@Parameter(
            description = "Intervalo de tempo (em minutos) para calcular as estatísticas. Se não informado, usa 1.",
            schema = @Schema(defaultValue = "1", minimum = "1")
    ) @RequestParam(required = false, defaultValue = "1") Integer tempoEmMinutos) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getStats(tempoEmMinutos));
    }

}
