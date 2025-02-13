package com.desafio.casepixitau.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Map;

/**
 * DTO para representar mensagens de erro na API.
 */
@Data
public class ErrorResponseDTO {

    /**
     * Mensagem geral de erro.
     */
    private String errorMessage;

    /**
     * Mapa contendo detalhes dos erros de validação.
     * Se for null, não será incluído no JSON.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    /**
     * Construtor para erros gerais.
     *
     * @param errorMessage Mensagem de erro.
     */
    public ErrorResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Construtor para erros de validação.
     *
     * @param errorMessage Mensagem geral de erro.
     * @param errors Mapa contendo detalhes dos erros de validação.
     */
    public ErrorResponseDTO(String errorMessage, Map<String, String> errors) {
        this.errorMessage = errorMessage;
        this.errors = errors;
    }
}
