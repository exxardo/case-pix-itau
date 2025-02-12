package com.desafio.casepixitau.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta para a alteração de uma chave Pix (sem o campo de inativação).
 */
@Data
public class ChavePixAlteracaoDTO {

    private UUID id;
    private String tipoChave;
    private String valorChave;
    private String tipoConta;
    private int numeroAgencia;
    private int numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private LocalDateTime dataHoraInclusao;
}

