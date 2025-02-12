package com.desafio.casepixitau.exception;

/**
 * Exceção personalizada para operações relacionadas à Chave Pix.
 * Estende RuntimeException para indicar erros em tempo de execução.
 */
public class ChavePixException extends RuntimeException {

    /**
     * Construtor da exceção que recebe uma mensagem descritiva do erro.
     *
     * @param message Mensagem de erro associada à exceção.
     */
    public ChavePixException(String message) {
        super(message);
    }
}

