package com.desafio.casepixitau;

import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.dto.ChavePixResponseDTO;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.ChavePixRepository;
import com.desafio.casepixitau.service.ChavePixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes para validar as funcionalidades do serviço de cadastro de chaves PIX.
 * Os testes são implementados utilizando JUnit 5 e Mockito para mockar as dependências.
 */
class CasePixItauApplicationTests {

    @Mock
    private ChavePixRepository repository;

    @InjectMocks
    private ChavePixService service;

    private ChavePixRequestDTO requestDTO;
    private ChavePix chavePix;

    /**
     * Configuração inicial dos objetos antes da execução de cada teste.
     * Inicializa o Mockito, configura a requisição de exemplo e a entidade ChavePix.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("cpf");
        requestDTO.setValorChave("12345678901");
        requestDTO.setTipoConta("corrente");
        requestDTO.setNumeroAgencia(1234);
        requestDTO.setNumeroConta(56789012);
        requestDTO.setNomeCorrentista("João");
        requestDTO.setSobrenomeCorrentista("Silva");

        chavePix = new ChavePix();
        chavePix.setId(UUID.randomUUID());
        chavePix.setTipoChave(requestDTO.getTipoChave());
        chavePix.setValorChave(requestDTO.getValorChave());
        chavePix.setTipoConta(requestDTO.getTipoConta());
        chavePix.setNumeroAgencia(requestDTO.getNumeroAgencia());
        chavePix.setNumeroConta(requestDTO.getNumeroConta());
        chavePix.setNomeCorrentista(requestDTO.getNomeCorrentista());
        chavePix.setSobrenomeCorrentista(requestDTO.getSobrenomeCorrentista());
        chavePix.setDataHoraInclusao(LocalDateTime.now());
    }

    /**
     * Testa a inclusão de uma chave PIX válida do tipo CPF.
     * Verifica se a chave é salva corretamente e se o ID é gerado.
     */
    @Test
    void deveIncluirChaveCpfValida() {
        System.out.println("\n[Teste] Incluir Chave CPF Válida");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: Corpo da requisição com as informações persistidas.");

        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);

        ChavePixResponseDTO response = service.incluir(requestDTO);

        System.out.println("Resultado Obtido: " + response);
        assertNotNull(response);
        assertEquals(requestDTO.getValorChave(), response.getValorChave());
        assertNotNull(response.getId());
        assertNotNull(response.getDataHoraInclusao());
        verify(repository, times(1)).save(any(ChavePix.class));
    }

    /**
     * Testa a tentativa de inclusão de um CPF inválido.
     * Deve lançar uma exceção de validação.
     */
    @Test
    void naoDeveIncluirCpfInvalido() {
        requestDTO.setValorChave("123"); // CPF inválido
        System.out.println("\n[Teste] Não Incluir CPF Inválido");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: CPF inválido.");

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        System.out.println("Resultado Obtido: " + exception.getMessage());
        assertEquals("CPF inválido.", exception.getMessage());
    }

    /**
     * Testa a inclusão de uma chave PIX válida do tipo e-mail.
     * Verifica se a chave é salva corretamente e se o formato do e-mail é respeitado.
     */
    @Test
    void deveIncluirChaveEmailValido() {
        requestDTO.setTipoChave("email");
        requestDTO.setValorChave("usuario@example.com");
        System.out.println("\n[Teste] Incluir Chave E-mail Válida");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: Corpo da requisição com as informações persistidas.");

        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);

        ChavePixResponseDTO response = service.incluir(requestDTO);

        System.out.println("Resultado Obtido: " + response);
        assertNotNull(response);
        assertEquals(requestDTO.getValorChave(), response.getValorChave());
        assertTrue(response.getValorChave().contains("@"));
    }

    /**
     * Testa a tentativa de inclusão de um e-mail sem o símbolo '@'.
     * Deve lançar uma exceção de validação.
     */
    @Test
    void naoDeveIncluirEmailSemArroba() {
        requestDTO.setTipoChave("email");
        requestDTO.setValorChave("usuarioexample.com"); // Email inválido
        System.out.println("\n[Teste] Não Incluir E-mail Sem '@'");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: E-mail inválido.");

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        System.out.println("Resultado Obtido: " + exception.getMessage());
        assertEquals("E-mail inválido.", exception.getMessage());
    }

    /**
     * Testa a inclusão de uma chave PIX válida do tipo celular.
     * Verifica se o formato do número está correto.
     */
    @Test
    void deveIncluirChaveCelularValido() {
        requestDTO.setTipoChave("celular");
        requestDTO.setValorChave("+5511999999999");
        System.out.println("\n[Teste] Incluir Chave Celular Válida");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: Corpo da requisição com as informações persistidas.");

        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);

        ChavePixResponseDTO response = service.incluir(requestDTO);

        System.out.println("Resultado Obtido: " + response);
        assertNotNull(response);
        assertEquals(requestDTO.getValorChave(), response.getValorChave());
    }

    /**
     * Testa a tentativa de inclusão de um celular sem o código do país.
     * Deve lançar uma exceção de validação.
     */
    @Test
    void naoDeveIncluirCelularSemCodigoPais() {
        requestDTO.setTipoChave("celular");
        requestDTO.setValorChave("11999999999"); // Sem código do país
        System.out.println("\n[Teste] Não Incluir Celular Sem Código do País");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: Celular inválido: deve conter o código do país iniciando com...");

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        System.out.println("Resultado Obtido: " + exception.getMessage());
        assertEquals("Celular inválido: deve conter o código do país iniciando com '+'.", exception.getMessage());
    }

    /**
     * Testa a tentativa de inclusão de uma chave PIX duplicada.
     * Deve lançar uma exceção de duplicidade.
     */
    @Test
    void naoDeveIncluirChaveDuplicada() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.of(chavePix));
        System.out.println("\n[Teste] Não Incluir Chave Duplicada");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: O valor da chave já está cadastrado.");

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        System.out.println("Resultado Obtido: " + exception.getMessage());
        assertEquals("O valor da chave já está cadastrado.", exception.getMessage());
    }

    /**
     * Testa o limite de inclusão de chaves PIX para uma conta.
     * Deve lançar uma exceção quando o limite é atingido.
     */
    @Test
    void naoDeveIncluirQuandoLimiteDeChavesAtingido() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(5L);
        System.out.println("\n[Teste] Não Incluir Quando Limite de Chaves Atingido");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: Limite de chaves atingido para esta conta.");

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        System.out.println("Resultado Obtido: " + exception.getMessage());
        assertEquals("Limite de chaves atingido para esta conta.", exception.getMessage());
    }

    /**
     * Testa o registro correto da data e hora de inclusão da chave PIX.
     */
    @Test
    void deveRegistrarDataHoraInclusao() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);
        System.out.println("\n[Teste] Registrar Data e Hora de Inclusão");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: Resultado Obtido: DataHoraInclusao = (Data e Hora em que o teste foi rodado)");

        ChavePixResponseDTO response = service.incluir(requestDTO);

        System.out.println("Resultado Obtido: DataHoraInclusao = " + response.getDataHoraInclusao());
        assertNotNull(response.getDataHoraInclusao());
    }

    /**
     * Testa se o ID da chave gerada está no formato UUID.
     */
    @Test
    void deveValidarFormatoUUIDParaId() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);
        System.out.println("\n[Teste] Validar Formato UUID para ID");
        System.out.println("Parâmetros: " + requestDTO);
        System.out.println("Resultado Esperado: ID no formato UUID (Exemplo: 22ed1a08-cfe8-4833-b8a0-945a0264beb6)");

        ChavePixResponseDTO response = service.incluir(requestDTO);

        System.out.println("Resultado Obtido: ID = " + response.getId());
        assertDoesNotThrow(() -> UUID.fromString(response.getId().toString()));
    }
}
