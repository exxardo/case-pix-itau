package com.desafio.casepixitau.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pelo tratamento global de exceções na aplicação.
 * Captura e formata erros de validação para um padrão de resposta consistente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manipula exceções de validação de argumentos em requisições.
     *
     * @param ex Exceção capturada quando um argumento de método não é válido.
     * @return ResponseEntity contendo um mapa de erros formatado.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        // Mantém a estrutura original de erros por campo
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        // Adiciona a mensagem geral sem quebrar compatibilidade
        response.put("errorMessage", "Erro de validação nos campos");
        response.put("errors", fieldErrors); // Mantém formato original

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}