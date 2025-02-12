package com.desafio.casepixitau;

import com.desafio.casepixitau.controller.ChavePixController;
import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.dto.ChavePixResponseDTO;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.service.ChavePixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o {@link ChavePixController}.
 *
 * Esses testes validam o comportamento dos endpoints do controlador para diferentes cenários,
 * como inclusão, consulta, alteração e inativação de chaves Pix.
 * Também cobre casos de sucesso e falha, garantindo o tratamento adequado de exceções.
 */
public class ChavePixControllerTests {

    @Mock
    private ChavePixService service; // Mock do serviço para isolar o controlador nos testes

    @InjectMocks
    private ChavePixController controller; // Instancia o controlador com o serviço mockado

    private ChavePixRequestDTO requestDTO;
    private ChavePixResponseDTO responseDTO;

    /**
     * Configuração inicial antes de cada teste.
     * Inicializa os mocks e configura objetos de teste.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Dados de entrada para os testes
        requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("email");
        requestDTO.setValorChave("teste@email.com");
        requestDTO.setTipoConta("corrente");
        requestDTO.setNumeroAgencia(1234);
        requestDTO.setNumeroConta(567890);
        requestDTO.setNomeCorrentista("Joao");
        requestDTO.setSobrenomeCorrentista("Silva");

        // Dados esperados como resposta dos testes
        responseDTO = new ChavePixResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setTipoChave("email");
        responseDTO.setValorChave("teste@email.com");
        responseDTO.setTipoConta("corrente");
        responseDTO.setNumeroAgencia(1234);
        responseDTO.setNumeroConta(567890);
        responseDTO.setNomeCorrentista("Joao");
        responseDTO.setSobrenomeCorrentista("Silva");
    }

    /**
     * Testa a inclusão de uma chave Pix com sucesso.
     */
    @Test
    public void testIncluirChavePixComSucesso() {
        when(service.incluir(any(ChavePixRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ChavePixResponseDTO> response = controller.incluir(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("teste@email.com", response.getBody().getValorChave());
    }

    /**
     * Testa a inclusão de uma chave Pix que resulta em erro.
     */
    @Test
    public void testIncluirChavePixComErro() {
        when(service.incluir(any(ChavePixRequestDTO.class))).thenThrow(new ChavePixException("Erro ao incluir"));

        ResponseEntity<ChavePixResponseDTO> response = controller.incluir(requestDTO);

        assertEquals(422, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    /**
     * Testa a consulta de uma chave Pix por ID com sucesso.
     */
    @Test
    public void testConsultarChavePixPorIdComSucesso() {
        when(service.consultarPorId(any(UUID.class))).thenReturn(responseDTO);

        ResponseEntity<ChavePixResponseDTO> response = controller.consultarPorId(UUID.randomUUID());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("teste@email.com", response.getBody().getValorChave());
    }

    /**
     * Testa a consulta de uma chave Pix por ID quando a chave não é encontrada.
     */
    @Test
    public void testConsultarChavePixPorIdNaoEncontrado() {
        when(service.consultarPorId(any(UUID.class))).thenThrow(new ChavePixException("Chave Pix não encontrada."));

        ResponseEntity<ChavePixResponseDTO> response = controller.consultarPorId(UUID.randomUUID());

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    /**
     * Testa a alteração de uma chave Pix com sucesso.
     */
    @Test
    public void testAlterarChavePixComSucesso() {
        when(service.alterar(any(UUID.class), any(ChavePixRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ChavePixResponseDTO> response = controller.alterar(UUID.randomUUID(), requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("teste@email.com", response.getBody().getValorChave());
    }

    /**
     * Testa a alteração de uma chave Pix que resulta em erro.
     */
    @Test
    public void testAlterarChavePixComErro() {
        when(service.alterar(any(UUID.class), any(ChavePixRequestDTO.class))).thenThrow(new ChavePixException("Erro ao alterar"));

        ResponseEntity<ChavePixResponseDTO> response = controller.alterar(UUID.randomUUID(), requestDTO);

        assertEquals(422, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    /**
     * Testa a inativação de uma chave Pix com sucesso.
     */
    @Test
    public void testInativarChavePixComSucesso() {
        when(service.inativar(any(UUID.class))).thenReturn(responseDTO);

        ResponseEntity<ChavePixResponseDTO> response = controller.inativar(UUID.randomUUID());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("teste@email.com", response.getBody().getValorChave());
    }

    /**
     * Testa a inativação de uma chave Pix que resulta em erro.
     */
    @Test
    public void testInativarChavePixComErro() {
        when(service.inativar(any(UUID.class))).thenThrow(new ChavePixException("Erro ao inativar"));

        ResponseEntity<ChavePixResponseDTO> response = controller.inativar(UUID.randomUUID());

        assertEquals(422, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    /**
     * Testa a consulta de chaves Pix por tipo.
     */
    @Test
    public void testConsultarPorTipoChave() {
        when(service.consultarPorTipoChave(anyString())).thenReturn(Collections.singletonList(responseDTO));

        ResponseEntity<List<ChavePixResponseDTO>> response = controller.consultarPorTipoChave("email");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("teste@email.com", response.getBody().get(0).getValorChave());
    }

    /**
     * Testa a consulta de chaves Pix por data de inclusão.
     */
    @Test
    public void testConsultarPorDataInclusao() {
        when(service.consultarPorData(any(LocalDate.class), isNull())).thenReturn(Collections.singletonList(responseDTO));

        ResponseEntity<List<ChavePixResponseDTO>> response = controller.consultarPorData(LocalDate.now(), null);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Testa a consulta de chaves Pix com erro quando datas inválidas são fornecidas.
     */
    @Test
    public void testConsultarPorDataComErro() {
        ResponseEntity<List<ChavePixResponseDTO>> response = controller.consultarPorData(LocalDate.now(), LocalDate.now());

        assertEquals(422, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
