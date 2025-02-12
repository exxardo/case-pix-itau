package com.desafio.casepixitau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para representar mensagens de erro.
 */
@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String mensagem;
}
