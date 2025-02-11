package com.desafio.casepixitau.repository;

import com.desafio.casepixitau.model.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositório específico para operações com ChavePix utilizando o banco de dados H2.
 * Extende JpaRepository para operações CRUD e herda métodos de ChavePixRepository.
 */
@Repository
public interface H2ChavePixRepository extends JpaRepository<ChavePix, UUID>, ChavePixRepository {
}