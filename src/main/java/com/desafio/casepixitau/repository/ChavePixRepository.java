package com.desafio.casepixitau.repository;

import com.desafio.casepixitau.model.ChavePix;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que define as operações para manipulação das chaves Pix.
 * Pode ser implementada por diferentes repositórios para fornecer persistência.
 */
public interface ChavePixRepository {

    /**
     * Busca uma chave Pix pelo seu ID.
     *
     * @param id Identificador único da chave Pix.
     * @return Um Optional contendo a chave Pix, se encontrada.
     */
    Optional<ChavePix> findById(UUID id);

    /**
     * Busca uma chave Pix pelo seu valor.
     *
     * @param valorChave O valor da chave Pix.
     * @return Um Optional contendo a chave Pix, se encontrada.
     */
    Optional<ChavePix> findByValorChave(String valorChave);

    /**
     * Busca chaves Pix pelo tipo de chave.
     *
     * @param tipoChave Tipo da chave Pix.
     * @return Lista de chaves Pix do tipo especificado.
     */
    List<ChavePix> findByTipoChave(String tipoChave);

    /**
     * Busca chaves Pix pela agência e conta.
     *
     * @param numeroAgencia Número da agência.
     * @param numeroConta Número da conta.
     * @return Lista de chaves Pix que correspondem à agência e conta informadas.
     */
    List<ChavePix> findByNumeroAgenciaAndNumeroConta(int numeroAgencia, int numeroConta);

    /**
     * Busca chaves Pix pela data de inclusão.
     *
     * @param inicio Data e hora inicial.
     * @param fim Data e hora final.
     * @return Lista de chaves Pix incluídas no período especificado.
     */
    List<ChavePix> findByDataHoraInclusaoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca chaves Pix pela data de inativação.
     *
     * @param inicio Data e hora inicial.
     * @param fim Data e hora final.
     * @return Lista de chaves Pix inativadas no período especificado.
     */
    List<ChavePix> findByDataHoraInativacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta a quantidade de chaves para uma agência e conta.
     *
     * @param numeroAgencia Número da agência.
     * @param numeroConta Número da conta.
     * @return Quantidade de chaves associadas à agência e conta.
     */
    long countByNumeroAgenciaAndNumeroConta(int numeroAgencia, int numeroConta);

    /**
     * Salva uma chave Pix no repositório.
     *
     * @param chavePix Objeto ChavePix a ser salvo.
     * @return A chave Pix persistida.
     */
    ChavePix save(ChavePix chavePix);

    /**
     * Remove uma chave Pix pelo seu ID.
     *
     * @param id Identificador único da chave Pix a ser removida.
     */
    void deleteById(UUID id);

    /**
     * Conta o número de chaves Pix ativas associadas a uma agência e conta específicas.
     * Uma chave é considerada ativa quando o campo {@code dataHoraInativacao} está nulo.
     *
     * @param numeroAgencia o número da agência bancária associada às chaves Pix.
     * @param numeroConta o número da conta bancária associada às chaves Pix.
     * @return a quantidade de chaves Pix ativas (com {@code dataHoraInativacao} nulo) para a combinação de agência e conta fornecida.
     */
    long countByNumeroAgenciaAndNumeroContaAndDataHoraInativacaoIsNull(int numeroAgencia, int numeroConta);

}
