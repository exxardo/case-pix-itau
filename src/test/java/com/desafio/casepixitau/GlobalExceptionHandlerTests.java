package com.desafio.casepixitau;

import com.desafio.casepixitau.util.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTests {

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions_DeveRetornarErrosFormatadosCorretamente() {
        // Configuração dos mocks
        FieldError fieldError = new FieldError(
                "objectName",
                "fieldName",
                "Mensagem de erro"
        );

        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // Criando a exceção com o mock de BindingResult
        exception = new MethodArgumentNotValidException(null, bindingResult);

        // Execução do método
        ResponseEntity<Map<String, Object>> response =
                handler.handleValidationExceptions(exception);

        // Verificações
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("Erro de validação nos campos", body.get("errorMessage"));

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals(1, errors.size());
        assertEquals("Mensagem de erro", errors.get("fieldName"));
    }

}
