package dev.tuchanski.transacoes.controllers;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.services.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping("/transacao")
    public ResponseEntity<Void> create(@RequestBody TransacaoRequestDTO dto) {
        this.service.createTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<Void> deleteAll() {
        this.service.clearTransacoes();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<HashMap<String, Number>> getStatsUltimoMin() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getStatsUltimoMinuto());
    }

}
