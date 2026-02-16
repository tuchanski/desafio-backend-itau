package dev.tuchanski.transacoes.controllers;

import dev.tuchanski.transacoes.dtos.TransacaoRequestDTO;
import dev.tuchanski.transacoes.services.impl.TransacaoServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class TransacaoController {

    @Autowired
    private final TransacaoServiceImpl service;

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
