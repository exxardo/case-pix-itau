package com.desafio.casepixitau;

import com.desafio.casepixitau.exception.ChavePixException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link ChavePixException}.
 *
 * Esses testes garantem que a exceção personalizada {@link ChavePixException}
 * esteja funcionando corretamente, validando tanto a mensagem de erro quanto a
 * hierarquia da exceção.
 */
public class ChavePixExceptionTests {

    /**
     * Testa se a mensagem fornecida na criação da {@link ChavePixException}
     * é corretamente retornada.
     */
    @Test
    public void testChavePixExceptionMessage() {
        ChavePixException exception = new ChavePixException("Erro ao processar a chave PIX");

        // Verifica se a mensagem da exceção corresponde à esperada
        assertEquals("Erro ao processar a chave PIX", exception.getMessage());
    }

    /**
     * Testa se a {@link ChavePixException} é uma instância de {@link RuntimeException}.
     */
    @Test
    public void testChavePixExceptionInstance() {
        ChavePixException exception = new ChavePixException("Erro genérico");

        // Verifica se a exceção é do tipo RuntimeException
        assertTrue(exception instanceof RuntimeException);
    }
}
