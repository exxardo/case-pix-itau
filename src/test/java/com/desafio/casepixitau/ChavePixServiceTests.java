package com.desafio.casepixitau;

import com.desafio.casepixitau.controller.ChavePixController;
import com.desafio.casepixitau.dto.ChavePixAlteracaoDTO;
import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.dto.ChavePixResponseDTO;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.service.ChavePixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ChavePixServiceTests {

    @Mock
    private ChavePixService service; // Mock do serviço

    @InjectMocks
    private ChavePixController controller; // Controlador que será testado, injetando o serviço

    private ChavePixRequestDTO requestDTO;
    private ChavePixAlteracaoDTO alteracaoDTO;
    private ChavePix chavePix;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Configuração do DTO para inclusão
        requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("cpf");
        requestDTO.setValorChave("12345678900");
        requestDTO.setTipoConta("corrente");
        requestDTO.setNumeroAgencia(1234);
        requestDTO.setNumeroConta(567890);
        requestDTO.setNomeCorrentista("Joao");
        requestDTO.setSobrenomeCorrentista("Silva");

        // Configuração do DTO para alteração
        alteracaoDTO = new ChavePixAlteracaoDTO();
        alteracaoDTO.setTipoChave("email");
        alteracaoDTO.setValorChave("novovalor@email.com");

        // Configuração da chave Pix
        chavePix = new ChavePix();
        chavePix.setId(UUID.randomUUID());
        chavePix.setTipoChave("cpf");
        chavePix.setValorChave("12345678900");
        chavePix.setTipoConta("corrente");
        chavePix.setNumeroAgencia(1234);
        chavePix.setNumeroConta(567890);
        chavePix.setNomeCorrentista("Joao");
        chavePix.setSobrenomeCorrentista("Silva");
        chavePix.setDataHoraInativacao(null); // Inicialmente ativa
    }

    @Test
    public void testConsultarPorTipoChaveComSucesso() {
        // Criando a chave para retornar no mock
        ChavePix chavePix = new ChavePix();
        chavePix.setId(UUID.randomUUID());
        chavePix.setTipoChave("email");
        chavePix.setValorChave("teste@email.com");
        chavePix.setTipoConta("corrente");
        chavePix.setNumeroAgencia(1234);
        chavePix.setNumeroConta(567890);
        chavePix.setNomeCorrentista("Joao");
        chavePix.setSobrenomeCorrentista("Silva");

        // Convertendo ChavePix para ChavePixResponseDTO
        ChavePixResponseDTO responseDTO = new ChavePixResponseDTO();
        responseDTO.setId(chavePix.getId());
        responseDTO.setTipoChave(chavePix.getTipoChave());
        responseDTO.setValorChave(chavePix.getValorChave());
        responseDTO.setTipoConta(chavePix.getTipoConta());
        responseDTO.setNumeroAgencia(chavePix.getNumeroAgencia());
        responseDTO.setNumeroConta(chavePix.getNumeroConta());
        responseDTO.setNomeCorrentista(chavePix.getNomeCorrentista());
        responseDTO.setSobrenomeCorrentista(chavePix.getSobrenomeCorrentista());

        // Mocking o comportamento do serviço
        when(service.consultarPorTipoChave(any(String.class))).thenReturn(Collections.singletonList(responseDTO));

        // Chamando o controlador e validando a resposta
        ResponseEntity<List<ChavePixResponseDTO>> response = controller.consultarPorTipoChave("email");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("teste@email.com", response.getBody().get(0).getValorChave());
    }
}
