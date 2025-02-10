package com.desafio.casepixitau.repository;

import com.desafio.casepixitau.model.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável por operações de persistência da entidade ChavePix.
 * Extende JpaRepository para fornecer operações CRUD padrão.
 */
@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, UUID> {

    /**
     * Busca uma ChavePix pelo seu valor único.
     *
     * @param valorChave o valor da chave Pix a ser buscado.
     * @return um Optional contendo a ChavePix, caso exista.
     */
    Optional<ChavePix> findByValorChave(String valorChave);
}
