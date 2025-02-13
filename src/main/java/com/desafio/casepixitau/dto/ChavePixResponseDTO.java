package com.desafio.casepixitau.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para representar a resposta de uma Chave Pix.
 */
@AllArgsConstructor
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

    /**
     * Data e hora da inclusão da chave. Pode ser nula.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dataHoraInclusao;

    /**
     * Data e hora da inativação da chave. Retorna uma string vazia se for nula.
     */
    private LocalDateTime dataHoraInativacao;

    public ChavePixResponseDTO() {
    }

    /**
     * Obtém a data e hora de inativação, retornando uma string vazia se for nula.
     *
     * @return Data e hora da inativação ou "" se for nula.
     */
    @JsonProperty("dataHoraInativacao")
    public String getDataHoraInativacao() {
        return dataHoraInativacao == null ? "" : dataHoraInativacao.toString();
    }

    // Getters e setters com tratamento para evitar valores nulos retornando string vazia
    public String getTipoChave() {
        return tipoChave == null ? "" : tipoChave;
    }

    public String getValorChave() {
        return valorChave == null ? "" : valorChave;
    }

    public String getTipoConta() {
        return tipoConta == null ? "" : tipoConta;
    }

    public String getNomeCorrentista() {
        return nomeCorrentista == null ? "" : nomeCorrentista;
    }

    public String getSobrenomeCorrentista() {
        return sobrenomeCorrentista == null ? "" : sobrenomeCorrentista;
    }
}