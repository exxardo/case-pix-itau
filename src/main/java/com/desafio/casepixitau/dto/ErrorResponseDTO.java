package com.desafio.casepixitau.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {

    private String errorMessage;

    public ErrorResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}