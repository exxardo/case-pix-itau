package com.desafio.casepixitau.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta para a alteração de uma chave Pix (sem o campo de inativação).
 */
@Data
public class ChavePixAlteracaoDTO {

    private UUID id;

    private String tipoChave;

    private String valorChave;

    /**
     * Tipo de conta associada à chave Pix (corrente ou poupança).
     */
    @NotBlank(message = "O tipo de conta é obrigatório.")
    @Pattern(regexp = "corrente|poupança", message = "O tipo de conta deve ser 'corrente' ou 'poupança'.")
    private String tipoConta;

    /**
     * Número da agência bancária, deve ter no máximo 4 dígitos.
     */
    @Min(value = 1, message = "O número da agência deve ser positivo e ter no máximo 4 dígitos.")
    @Max(value = 9999, message = "O número da agência deve ter no máximo 4 dígitos.")
    private int numeroAgencia;

    /**
     * Número da conta bancária, deve ter no máximo 8 dígitos.
     */
    @Min(value = 1, message = "O número da conta deve ser positivo e ter no máximo 8 dígitos.")
    @Max(value = 99999999, message = "O número da conta deve ter no máximo 8 dígitos.")
    private int numeroConta;

    /**
     * Nome do correntista associado à chave Pix, com no máximo 30 caracteres.
     */
    @NotBlank(message = "O nome do correntista é obrigatório e deve ter no máximo 30 caracteres.")
    @Size(max = 30, message = "O nome do correntista deve ter no máximo 30 caracteres.")
    private String nomeCorrentista;

    /**
     * Sobrenome do correntista associado à chave Pix, com no máximo 45 caracteres.
     */
    @Size(max = 45, message = "O sobrenome do correntista deve ter no máximo 45 caracteres.")
    private String sobrenomeCorrentista;

    private LocalDateTime dataHoraInclusao;
}
