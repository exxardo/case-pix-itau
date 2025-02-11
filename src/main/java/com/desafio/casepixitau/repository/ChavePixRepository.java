package com.desafio.casepixitau.repository;

import com.desafio.casepixitau.model.ChavePix;
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
}