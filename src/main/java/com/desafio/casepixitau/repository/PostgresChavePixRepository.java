//package com.desafio.casepixitau.repository;
//
//import com.desafio.casepixitau.model.ChavePix;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//import java.util.UUID;
//
///**
// * Implementação do repositório de ChavePix para PostgreSQL.
// */
//@Repository
//@Profile("postgres")
//public interface PostgresChavePixRepository extends JpaRepository<ChavePix, UUID>, ChavePixRepository {
//
//    /**
//     * Busca uma chave PIX pelo valor.
//     *
//     * @param valorChave valor da chave PIX.
//     * @return um Optional contendo a chave PIX, se encontrada.
//     */
//    @Override
//    Optional<ChavePix> findByValorChave(String valorChave);
//
//    /**
//     * Verifica se uma chave PIX já existe pelo valor.
//     *
//     * @param valorChave valor da chave PIX.
//     * @return true se a chave existir, false caso contrário.
//     */
//    boolean existsByValorChave(String valorChave);
//}
