package com.desafio.casepixitau;

import com.desafio.casepixitau.model.ChavePix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe ChavePix.
 */
class ChavePixTests {

    private ChavePix chavePix;

    @BeforeEach
    void setUp() {
        chavePix = new ChavePix();
        chavePix.setId(UUID.randomUUID());
        chavePix.setTipoChave("CPF");
        chavePix.setValorChave("12345678900");
        chavePix.setTipoConta("Corrente");
        chavePix.setNumeroAgencia(1234);
        chavePix.setNumeroConta(567890);
        chavePix.setNomeCorrentista("João");
        chavePix.setSobrenomeCorrentista("Silva");
        chavePix.setDataHoraInclusao(LocalDateTime.now());
        chavePix.setVersion(1L);
    }

    @Test
    void testGettersAndSetters() {
        assertNotNull(chavePix.getId(), "ID deve estar definido");
        assertEquals("CPF", chavePix.getTipoChave());
        assertEquals("12345678900", chavePix.getValorChave());
        assertEquals("Corrente", chavePix.getTipoConta());
        assertEquals(1234, chavePix.getNumeroAgencia());
        assertEquals(567890, chavePix.getNumeroConta());
        assertEquals("João", chavePix.getNomeCorrentista());
        assertEquals("Silva", chavePix.getSobrenomeCorrentista());
        assertNotNull(chavePix.getDataHoraInclusao(), "Data de inclusão deve estar definida");
        assertEquals(1L, chavePix.getVersion());
    }

    @Test
    void testDataHoraInativacao() {
        assertNull(chavePix.getDataHoraInativacao(), "Data de inativação deve ser nula inicialmente");
        LocalDateTime now = LocalDateTime.now();
        chavePix.setDataHoraInativacao(now);
        assertEquals(now, chavePix.getDataHoraInativacao(), "Data de inativação deve ser atualizada corretamente");
    }

    @Test
    void testEquality() {
        ChavePix otherChavePix = new ChavePix();
        otherChavePix.setId(chavePix.getId());
        assertEquals(chavePix.getId(), otherChavePix.getId(), "IDs iguais devem ser considerados iguais");
    }

    @Test
    void testToString() {
        String toString = chavePix.toString();
        assertTrue(toString.contains("tipoChave='CPF'"), "toString deve conter o tipo de chave");
        assertTrue(toString.contains("valorChave='12345678900'"), "toString deve conter o valor da chave");
        assertTrue(toString.contains("tipoConta='Corrente'"), "toString deve conter o tipo de conta");
        assertTrue(toString.contains("nomeCorrentista='João'"), "toString deve conter o nome do correntista");
    }

    @Test
    void testSetValoresNulos() {
        chavePix.setSobrenomeCorrentista(null);
        assertNull(chavePix.getSobrenomeCorrentista(), "Sobrenome deve permitir valores nulos");

        chavePix.setDataHoraInativacao(null);
        assertNull(chavePix.getDataHoraInativacao(), "Data de inativação deve aceitar valores nulos");
    }

    @Test
    void testInativarChave() {
        assertNull(chavePix.getDataHoraInativacao(), "Chave deve estar ativa inicialmente");
        chavePix.setDataHoraInativacao(LocalDateTime.now());
        assertNotNull(chavePix.getDataHoraInativacao(), "Chave deve estar inativa após definição da data de inativação");
    }

    @Test
    void testUUIDGeneration() {
        UUID id = UUID.randomUUID();
        chavePix.setId(id);
        assertEquals(id, chavePix.getId(), "O UUID deve ser definido corretamente");
    }

    @Test
    void testVersionIncrement() {
        chavePix.setVersion(2L);
        assertEquals(2L, chavePix.getVersion(), "Versão deve ser incrementada corretamente");
    }
}
