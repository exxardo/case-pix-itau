package com.desafio.casepixitau;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para garantir o correto carregamento do contexto e das configurações do application.properties,
 * bem como a disponibilidade dos beans essenciais para a aplicação.
 */
@SpringBootTest
class CasePixItauApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriver;

    @Test
    void contextLoads() {
        // Teste básico para garantir que o contexto carrega sem erros
        assertNotNull(applicationContext, "O contexto da aplicação não foi carregado corretamente.");
    }

    @Test
    void testApplicationPropertiesLoaded() {
        // Testa se as propriedades essenciais do application.properties foram carregadas corretamente
        assertNotNull(datasourceUrl, "Datasource URL não foi carregada.");
        assertNotNull(datasourceUsername, "Datasource Username não foi carregado.");
        assertNotNull(datasourceDriver, "Datasource Driver não foi carregado.");

        // Verifica se a URL está em um formato válido para qualquer banco de dados
        assertTrue(datasourceUrl.startsWith("jdbc:"), "Datasource URL não está em um formato JDBC válido.");
    }

    @Test
    void testDataSourceBeanLoaded() {
        // Verifica se o bean DataSource está corretamente configurado e carregado
        assertNotNull(dataSource, "O bean DataSource não foi carregado corretamente.");
        assertDoesNotThrow(() -> dataSource.getConnection(), "Não foi possível obter conexão do DataSource.");
    }

    @Test
    void testApplicationRuns() {
        // Testa se a aplicação inicia sem erros
        assertDoesNotThrow(() -> SpringApplication.run(CasePixItauApplication.class), "A aplicação não iniciou corretamente.");
    }
}

