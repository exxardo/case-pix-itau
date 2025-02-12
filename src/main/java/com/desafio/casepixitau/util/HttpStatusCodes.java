package com.desafio.casepixitau.util;

import org.springframework.http.HttpStatus;

/**
 * Classe utilitária que centraliza os códigos de status HTTP usados na aplicação.
 * Não deve ser instanciada.
 */
public final class HttpStatusCodes {

    public static final HttpStatus SUCCESS = HttpStatus.OK;
    public static final HttpStatus UNPROCESSABLE_ENTITY = HttpStatus.UNPROCESSABLE_ENTITY;
    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    /**
     * Construtor privado para evitar instanciamento da classe.
     * Lança uma UnsupportedOperationException caso alguém tente instanciar.
     */
    private HttpStatusCodes() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }
}

