package com.desafio.casepixitau;

import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o {@link ChavePixRequestDTO}.
 *
 * Esses testes garantem que as validações de restrições (constraints) aplicadas ao DTO estão funcionando corretamente.
 * Testa casos válidos e inválidos para os campos obrigatórios e limites de valores.
 */
public class ChavePixRequestDTOTests {

    private Validator validator; // Validador para aplicar as restrições nos campos do DTO

    /**
     * Inicializa o validador antes de cada teste.
     * Garante que o contexto de validação está pronto para cada caso de teste.
     */
    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Testa um {@link ChavePixRequestDTO} com todos os campos válidos.
     * Deve passar sem nenhuma violação de restrição.
     */
    @Test
    public void testValidChavePixRequest() {
        ChavePixRequestDTO dto = new ChavePixRequestDTO();
        dto.setTipoChave("email");
        dto.setValorChave("teste@email.com");
        dto.setTipoConta("corrente");
        dto.setNumeroAgencia(1234);
        dto.setNumeroConta(567890);
        dto.setNomeCorrentista("Joao");

        Set<ConstraintViolation<ChavePixRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Espera-se que não haja violações para dados válidos.");
    }

    /**
     * Testa a ausência do campo "tipoChave" no {@link ChavePixRequestDTO}.
     * Deve gerar uma violação indicando que o campo é obrigatório.
     */
    @Test
    public void testMissingTipoChave() {
        ChavePixRequestDTO dto = new ChavePixRequestDTO();
        dto.setValorChave("teste@email.com");
        dto.setTipoConta("corrente");
        dto.setNumeroAgencia(1234);
        dto.setNumeroConta(567890);
        dto.setNomeCorrentista("Joao");

        Set<ConstraintViolation<ChavePixRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Deve haver violações para tipoChave ausente.");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Tipo da chave é obrigatório.")));
    }

    /**
     * Testa um valor inválido para o campo "numeroAgencia".
     * Deve gerar uma violação indicando que o número da agência deve ser positivo.
     */
    @Test
    public void testNumeroAgenciaInvalido() {
        ChavePixRequestDTO dto = new ChavePixRequestDTO();
        dto.setTipoChave("email");
        dto.setValorChave("teste@email.com");
        dto.setTipoConta("corrente");
        dto.setNumeroAgencia(-1); // Valor inválido
        dto.setNumeroConta(567890);
        dto.setNomeCorrentista("Joao");

        Set<ConstraintViolation<ChavePixRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Deve haver violações para numeroAgencia inválido.");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Número da agência deve ser positivo.")));
    }

    /**
     * Testa um valor excedente para o campo "numeroConta".
     * Deve gerar uma violação indicando que o número da conta deve ter no máximo 8 dígitos.
     */
    @Test
    public void testNumeroContaExcedido() {
        ChavePixRequestDTO dto = new ChavePixRequestDTO();
        dto.setTipoChave("email");
        dto.setValorChave("teste@email.com");
        dto.setTipoConta("corrente");
        dto.setNumeroAgencia(1234);
        dto.setNumeroConta(999999999); // Excede o limite de 8 dígitos
        dto.setNomeCorrentista("Joao");

        Set<ConstraintViolation<ChavePixRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Deve haver violações para numeroConta excedido.");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Número da conta deve ter no máximo 8 dígitos.")));
    }
}
