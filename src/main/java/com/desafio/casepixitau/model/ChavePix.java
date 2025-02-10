package com.desafio.casepixitau.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa uma chave Pix cadastrada no sistema.
 */
@Entity
public class ChavePix {

    @Id
    @GeneratedValue
    private UUID id; // Identificador único da chave Pix

    @Column(nullable = false)
    private String tipoChave; // Tipo da chave Pix (CPF, telefone ou e-mail)

    @Column(nullable = false, unique = true)
    private String valorChave; // Valor da chave Pix, deve ser único no sistema

    @Column(nullable = false)
    private String tipoConta; // Tipo da conta associada (corrente ou poupança)

    @Column(nullable = false)
    private int numeroAgencia; // Número da agência bancária

    @Column(nullable = false)
    private int numeroConta; // Número da conta bancária

    @Column(nullable = false)
    private String nomeCorrentista; // Nome do titular da conta

    private String sobrenomeCorrentista; // Sobrenome do titular da conta

    @Column(nullable = false)
    private LocalDateTime dataHoraInclusao; // Data e hora de criação do registro

    private LocalDateTime dataHoraInativacao; // Data e hora da inativação da chave (caso tenha sido desativada)

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(String tipoChave) {
        this.tipoChave = tipoChave;
    }

    public String getValorChave() {
        return valorChave;
    }

    public void setValorChave(String valorChave) {
        this.valorChave = valorChave;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public int getNumeroAgencia() {
        return numeroAgencia;
    }

    public void setNumeroAgencia(int numeroAgencia) {
        this.numeroAgencia = numeroAgencia;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getNomeCorrentista() {
        return nomeCorrentista;
    }

    public void setNomeCorrentista(String nomeCorrentista) {
        this.nomeCorrentista = nomeCorrentista;
    }

    public String getSobrenomeCorrentista() {
        return sobrenomeCorrentista;
    }

    public void setSobrenomeCorrentista(String sobrenomeCorrentista) {
        this.sobrenomeCorrentista = sobrenomeCorrentista;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public LocalDateTime getDataHoraInativacao() {
        return dataHoraInativacao;
    }

    public void setDataHoraInativacao(LocalDateTime dataHoraInativacao) {
        this.dataHoraInativacao = dataHoraInativacao;
    }
}

