package com.desafio.casepixitau.util;

import org.springframework.http.HttpStatus;

/**
 * Classe utilitária para centralizar os códigos de status HTTP utilizados na aplicação.
 */
public class HttpStatusCodes {

    // Códigos de sucesso
    public static final HttpStatus SUCCESS = HttpStatus.OK; // 200

    // Códigos de erro para validações
    public static final HttpStatus UNPROCESSABLE_ENTITY = HttpStatus.UNPROCESSABLE_ENTITY; // 422
    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND; // 404

    private HttpStatusCodes() {
        // Construtor privado para evitar instanciação
    }
}
