package dev.tuchanski.transacoes.models;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transacao {
    private float valor;
    private OffsetDateTime dataHora;
}
