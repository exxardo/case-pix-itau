package com.desafio.casepixitau.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * Repositório específico para o banco H2.
 * Este repositório será usado como o principal durante os testes.
 */
@Repository
@Primary
public interface H2ChavePixRepository extends ChavePixRepository {
}