package com.desafio.casepixitau;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes para garantir o correto carregamento do contexto e das configurações do application.properties,
 * independente do banco de dados utilizado.
 */
@SpringBootTest
class CasePixItauApplicationTests {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Test
    void contextLoads() {
        // Teste básico para garantir que o contexto carrega sem erros
    }

    @Test
    void testApplicationPropertiesLoaded() {
        // Testa se as propriedades essenciais do application.properties foram carregadas corretamente
        assertNotNull(datasourceUrl, "Datasource URL não foi carregada.");
        assertNotNull(datasourceUsername, "Datasource Username não foi carregado.");

        // Verifica se a URL está em um formato válido para qualquer banco de dados
        assertTrue(datasourceUrl.startsWith("jdbc:"), "Datasource URL não está em um formato JDBC válido.");
    }
}
