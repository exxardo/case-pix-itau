package com.desafio.casepixitau.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para receber os dados de requisição para cadastro de uma Chave Pix.
 * Utiliza validações para garantir a integridade dos dados fornecidos.
 */
@Data
public class ChavePixRequestDTO {

    /**
     * Tipo da chave Pix (email, CPF ou celular).
     * Deve ser um desses valores.
     */
    @NotBlank(message = "Tipo da chave é obrigatório.")
    @Pattern(regexp = "email|cpf|celular", message = "O tipo da chave deve ser 'email', 'cpf' ou 'celular'.")
    private String tipoChave;

    /**
     * Valor da chave Pix.
     * Deve seguir um formato válido de acordo com o tipo da chave.
     */
    @NotBlank(message = "Valor da chave é obrigatório.")
    private String valorChave;

    /**
     * Tipo de conta associada à chave Pix (corrente ou poupança).
     * Não pode estar em branco e deve ser 'corrente' ou 'poupança'.
     */
    @NotBlank(message = "Tipo da conta é obrigatório.")
    @Pattern(regexp = "corrente|poupança", message = "O tipo de conta deve ser 'corrente' ou 'poupança'.")
    private String tipoConta;

    /**
     * Número da agência bancária.
     * Deve ser positivo e ter no máximo 4 dígitos.
     */
    @Min(value = 1, message = "Número da agência deve ser positivo.")
    @Max(value = 9999, message = "Número da agência deve ter no máximo 4 dígitos.")
    private int numeroAgencia;

    /**
     * Número da conta bancária.
     * Deve ser positivo e ter no máximo 8 dígitos.
     */
    @Min(value = 1, message = "Número da conta deve ser positivo.")
    @Max(value = 99999999, message = "Número da conta deve ter no máximo 8 dígitos.")
    private int numeroConta;

    /**
     * Nome do titular da conta.
     * Não pode estar em branco.
     */
    @NotBlank(message = "Nome do correntista é obrigatório.")
    private String nomeCorrentista;

    /**
     * Sobrenome do titular da conta (opcional).
     */
    private String sobrenomeCorrentista;

    /**
     * Validação personalizada para garantir que o email esteja no formato correto quando o tipoChave for 'email'.
     */
    @AssertTrue(message = "Formato de email inválido.")
    public boolean isEmailValid() {
        if ("email".equalsIgnoreCase(tipoChave)) {
            return valorChave != null && valorChave.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        }
        return true;
    }
}
