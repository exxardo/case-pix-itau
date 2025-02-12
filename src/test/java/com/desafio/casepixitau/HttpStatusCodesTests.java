package com.desafio.casepixitau;

import com.desafio.casepixitau.util.HttpStatusCodes;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para verificar a integridade da classe {@link HttpStatusCodes}.
 *
 * Estes testes garantem que os códigos de status HTTP estão corretamente definidos e que a classe
 * é projetada para não ser instanciada, seguindo o padrão de uma classe utilitária.
 */
public class HttpStatusCodesTests {

    /**
     * Verifica se o código de status de sucesso ({@link HttpStatus#OK}) está corretamente definido.
     */
    @Test
    public void testSuccessStatusCode() {
        assertEquals(HttpStatus.OK, HttpStatusCodes.SUCCESS, "O código de sucesso deve ser HttpStatus.OK");
    }

    /**
     * Verifica se o código de status para entidade não processável ({@link HttpStatus#UNPROCESSABLE_ENTITY}) está correto.
     */
    @Test
    public void testUnprocessableEntityStatusCode() {
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatusCodes.UNPROCESSABLE_ENTITY, "O código deve ser HttpStatus.UNPROCESSABLE_ENTITY");
    }

    /**
     * Verifica se o código de status para recurso não encontrado ({@link HttpStatus#NOT_FOUND}) está correto.
     */
    @Test
    public void testNotFoundStatusCode() {
        assertEquals(HttpStatus.NOT_FOUND, HttpStatusCodes.NOT_FOUND, "O código deve ser HttpStatus.NOT_FOUND");
    }

    /**
     * Testa se a classe {@link HttpStatusCodes} não pode ser instanciada, garantindo que seu construtor seja privado.
     */
    @Test
    public void testPrivateConstructor() throws Exception {
        var constructor = HttpStatusCodes.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), "O construtor deve ser privado");

        constructor.setAccessible(true);
        Exception exception = assertThrows(Exception.class, constructor::newInstance, "A instância da classe deve lançar uma exceção");
        assertNotNull(exception, "A exceção não deve ser nula");
    }
}

