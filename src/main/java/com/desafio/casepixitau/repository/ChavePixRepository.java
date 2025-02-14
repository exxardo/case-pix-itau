package com.desafio.casepixitau.repository;

import com.desafio.casepixitau.model.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que define as operações de acesso a dados para a entidade ChavePix.
 * Estende JpaRepository para fornecer operações CRUD padrão e permite a definição de consultas personalizadas.
 */
public interface ChavePixRepository extends JpaRepository<ChavePix, UUID> {

    /**
     * Busca uma chave Pix pelo valor da chave.
     *
     * @param valorChave Valor da chave Pix.
     * @return Um Optional contendo a ChavePix, se encontrada.
     */
    Optional<ChavePix> findByValorChave(String valorChave);

    /**
     * Retorna uma lista de chaves Pix pelo tipo de chave.
     *
     * @param tipoChave Tipo da chave Pix (e.g., email, telefone, CPF).
     * @return Lista de chaves Pix do tipo especificado.
     */
    List<ChavePix> findByTipoChave(String tipoChave);

    /**
     * Retorna uma lista de chaves Pix associadas a uma agência e conta.
     *
     * @param numeroAgencia Número da agência.
     * @param numeroConta Número da conta.
     * @return Lista de chaves Pix associadas à agência e conta especificadas.
     */
    List<ChavePix> findByNumeroAgenciaAndNumeroConta(int numeroAgencia, int numeroConta);

    /**
     * Busca chaves Pix com data de inclusão entre o intervalo especificado.
     *
     * @param inicio Data e hora de início.
     * @param fim Data e hora de fim.
     * @return Lista de chaves Pix incluídas no período especificado.
     */
    List<ChavePix> findByDataHoraInclusaoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca chaves Pix com data de inativação entre o intervalo especificado.
     *
     * @param inicio Data e hora de início.
     * @param fim Data e hora de fim.
     * @return Lista de chaves Pix inativadas no período especificado.
     */
    List<ChavePix> findByDataHoraInativacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta o número de chaves Pix ativas para uma agência e conta.
     *
     * @param numeroAgencia Número da agência.
     * @param numeroConta Número da conta.
     * @return Número de chaves Pix ativas.
     */
    long countByNumeroAgenciaAndNumeroContaAndDataHoraInativacaoIsNull(int numeroAgencia, int numeroConta);

    /**
     * Conta o número total de chaves Pix associadas a uma agência e conta.
     *
     * @param numeroAgencia Número da agência.
     * @param numeroConta Número da conta.
     * @return Número total de chaves Pix para a agência e conta especificadas.
     */
    long countByNumeroAgenciaAndNumeroConta(int numeroAgencia, int numeroConta);

    /**
     * Retorna uma lista de chaves Pix que correspondem ao nome do correntista.
     *
     * @param nomeCorrentista Nome do correntista.
     * @return Lista de chaves Pix associadas ao nome do correntista.
     */
    List<ChavePix> findByNomeCorrentistaContainingIgnoreCase(String nomeCorrentista);

    /**
     * Busca chaves PIX aplicando múltiplos filtros opcionais. Se um parâmetro for nulo,
     * o critério correspondente não será aplicado na consulta.
     *
     * Os filtros disponíveis incluem ID, tipo de chave, valor da chave, número da agência,
     * número da conta, data de inclusão e data de inativação.
     *
     * @param tipoChave O tipo da chave PIX (CPF, CNPJ, e-mail, telefone ou aleatória). Pode ser nulo.
     * @param valorChave O valor da chave PIX (exemplo: número de telefone, e-mail). Pode ser nulo.
     * @param agencia O número da agência bancária associada à chave PIX. Pode ser nulo.
     * @param conta O número da conta bancária associada à chave PIX. Pode ser nulo.
     * @param dataInclusao A data e hora de inclusão da chave PIX. Filtra registros com data maior ou igual ao valor informado. Pode ser nulo.
     * @param dataInativacao A data e hora de inativação da chave PIX. Filtra registros com data maior ou igual ao valor informado. Pode ser nulo.
     * @return Uma lista de {@link ChavePix} que atendem aos critérios fornecidos.
     */
    @Query("SELECT c FROM ChavePix c WHERE " +
            "(:tipoChave IS NULL OR c.tipoChave = :tipoChave) " +
            "AND (:valorChave IS NULL OR c.valorChave = :valorChave) " +
            "AND (:agencia IS NULL OR c.numeroAgencia = :agencia) " +
            "AND (:conta IS NULL OR c.numeroConta = :conta) " +
            "AND (:dataInclusao IS NULL OR c.dataHoraInclusao >= :dataInclusao) " +
            "AND (:dataInativacao IS NULL OR c.dataHoraInativacao >= :dataInativacao)")
    List<ChavePix> buscarPorFiltros(@Param("tipoChave") String tipoChave,
                                    @Param("valorChave") String valorChave,
                                    @Param("agencia") Integer agencia,
                                    @Param("conta") Integer conta,
                                    @Param("dataInclusao") LocalDateTime dataInclusao,
                                    @Param("dataInativacao") LocalDateTime dataInativacao);


}
