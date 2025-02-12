package com.desafio.casepixitau;

import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.H2ChavePixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para o repositório {@link H2ChavePixRepository} usando banco de dados H2.
 *
 * Esses testes validam as operações de CRUD e consultas personalizadas no repositório de chaves Pix.
 */
@DataJpaTest
public class ChavePixRepositoryTests {

    @Autowired
    private H2ChavePixRepository repository; // Repositório que será testado

    private ChavePix chavePix; // Objeto de teste que será usado nos métodos

    /**
     * Configuração inicial antes de cada teste.
     * Cria e salva uma chave Pix no banco de dados H2 para uso nos testes.
     */
    @BeforeEach
    public void setup() {
        chavePix = new ChavePix();
        chavePix.setTipoChave("email");
        chavePix.setValorChave("teste@email.com");
        chavePix.setTipoConta("corrente");
        chavePix.setNumeroAgencia(1234);
        chavePix.setNumeroConta(567890);
        chavePix.setNomeCorrentista("Joao");
        chavePix.setSobrenomeCorrentista("Silva");
        chavePix.setDataHoraInclusao(LocalDateTime.now());
        repository.save(chavePix);
    }

    /**
     * Testa a busca de uma chave Pix pelo ID.
     */
    @Test
    public void testFindById() {
        Optional<ChavePix> found = repository.findById(chavePix.getId());
        assertTrue(found.isPresent());
        assertEquals("teste@email.com", found.get().getValorChave());
    }

    /**
     * Testa a busca de uma chave Pix pelo valor da chave.
     */
    @Test
    public void testFindByValorChave() {
        Optional<ChavePix> found = repository.findByValorChave("teste@email.com");
        assertTrue(found.isPresent());
        assertEquals("Joao", found.get().getNomeCorrentista());
    }

    /**
     * Testa a busca de chaves Pix pelo tipo da chave.
     */
    @Test
    public void testFindByTipoChave() {
        List<ChavePix> found = repository.findByTipoChave("email");
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }

    /**
     * Testa a busca de chaves Pix por número da agência e da conta.
     */
    @Test
    public void testFindByAgenciaAndConta() {
        List<ChavePix> found = repository.findByNumeroAgenciaAndNumeroConta(1234, 567890);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }

    /**
     * Testa a contagem de chaves Pix ativas (não inativas) para uma agência e conta.
     */
    @Test
    public void testCountByAgenciaAndConta() {
        long count = repository.countByNumeroAgenciaAndNumeroContaAndDataHoraInativacaoIsNull(1234, 567890);
        assertEquals(1, count);
    }

    /**
     * Testa a exclusão de uma chave Pix pelo ID.
     */
    @Test
    public void testDeleteById() {
        repository.deleteById(chavePix.getId());
        Optional<ChavePix> found = repository.findById(chavePix.getId());
        assertFalse(found.isPresent());
    }

    /**
     * Testa a busca de chaves Pix incluídas dentro de um intervalo de datas.
     */
    @Test
    public void testFindByDataHoraInclusaoBetween() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<ChavePix> found = repository.findByDataHoraInclusaoBetween(start, end);
        assertFalse(found.isEmpty());
    }

    /**
     * Testa a busca de chaves Pix inativadas dentro de um intervalo de datas.
     */
    @Test
    public void testFindByDataHoraInativacaoBetween() {
        chavePix.setDataHoraInativacao(LocalDateTime.now()); // Inativa a chave Pix
        repository.save(chavePix);

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<ChavePix> found = repository.findByDataHoraInativacaoBetween(start, end);
        assertFalse(found.isEmpty());
    }
}
