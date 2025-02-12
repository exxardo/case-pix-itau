package com.desafio.casepixitau.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para representar a resposta de uma Chave Pix.
 * Contém os dados retornados após operações como consulta e cadastro.
 */
@Data
public class ChavePixResponseDTO {

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
