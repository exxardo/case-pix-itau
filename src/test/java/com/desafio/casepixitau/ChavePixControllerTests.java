package com.desafio.casepixitau;

import com.desafio.casepixitau.controller.ChavePixController;
import com.desafio.casepixitau.dto.*;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.service.ChavePixService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChavePixControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ChavePixService chavePixService;

    @InjectMocks
    private ChavePixController chavePixController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chavePixController).build();
        objectMapper = new ObjectMapper();
    }

    // Testes para inclusão de chave PIX com erros
// Erro comum em vários testes: Campos obrigatórios não preenchidos
    @Test
    void testIncluirChavePix_ChaveDuplicada() throws Exception {
        ChavePixRequestDTO requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("cpf");
        requestDTO.setValorChave("12345678909");
        // Faltou preencher campos obrigatórios:
        requestDTO.setTipoConta("corrente");
        requestDTO.setNumeroAgencia(1234);
        requestDTO.setNumeroConta(123456);
        requestDTO.setNomeCorrentista("João Silva");

        when(chavePixService.incluir(any()))
                .thenThrow(new ChavePixException("Chave duplicada"));

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity()); // 422
    }

    @Test
    void testIncluirChavePix_CpfInvalido() throws Exception {
        ChavePixRequestDTO requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("cpf");
        requestDTO.setValorChave("123"); // CPF inválido
        requestDTO.setTipoConta("corrente");
        requestDTO.setNumeroAgencia(1234);
        requestDTO.setNumeroConta(123456);
        requestDTO.setNomeCorrentista("João Silva"); // Campos obrigatórios

        when(chavePixService.incluir(any()))
                .thenThrow(new ChavePixException("CPF inválido"));

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity()) // 422
                .andExpect(jsonPath("$.errorMessage").value("CPF inválido"));
    }

    // Testes para alteração de chave PIX
    @Test
    void testAlterarChavePix_Sucesso() throws Exception {
        UUID chaveId = UUID.randomUUID();
        ChavePixAlteracaoDTO alteracaoDTO = new ChavePixAlteracaoDTO();
        alteracaoDTO.setValorChave("novoemail@example.com");
        alteracaoDTO.setNomeCorrentista("João Silva"); // Novo
        alteracaoDTO.setTipoConta("corrente"); // Novo
        alteracaoDTO.setNumeroAgencia(1234); // Novo
        alteracaoDTO.setNumeroConta(123456); // Novo

        ChavePixResponseDTO responseDTO = new ChavePixResponseDTO();
        responseDTO.setValorChave("novoemail@example.com");

        when(chavePixService.alterar(eq(chaveId), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/api/pix/" + chaveId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteracaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorChave").value("novoemail@example.com"));
    }

    @Test
    void testAlterarChavePix_ChaveInexistente() throws Exception {
        UUID chaveId = UUID.randomUUID();
        ChavePixAlteracaoDTO alteracaoDTO = new ChavePixAlteracaoDTO();
        alteracaoDTO.setValorChave("novoemail@exemplo.com");
        alteracaoDTO.setNumeroAgencia(1234); // Campos obrigatórios
        alteracaoDTO.setNumeroConta(123456);
        alteracaoDTO.setNomeCorrentista("João Silva");
        alteracaoDTO.setTipoConta("corrente");

        when(chavePixService.alterar(eq(chaveId), any()))
                .thenThrow(new ChavePixException("Chave não encontrada"));

        mockMvc.perform(put("/api/pix/" + chaveId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteracaoDTO)))
                .andExpect(status().isNotFound()); // 404
    }

    @Test
    void testAlterarChavePix_ChaveInativa() throws Exception {
        UUID chaveId = UUID.randomUUID();
        ChavePixAlteracaoDTO alteracaoDTO = new ChavePixAlteracaoDTO();
        alteracaoDTO.setValorChave("novo@email.com");
        alteracaoDTO.setNomeCorrentista("João Silva");
        alteracaoDTO.setTipoConta("corrente");
        alteracaoDTO.setNumeroAgencia(1234);
        alteracaoDTO.setNumeroConta(123456);

        when(chavePixService.alterar(eq(chaveId), any()))
                .thenThrow(new ChavePixException("Chave inativa"));

        mockMvc.perform(put("/api/pix/" + chaveId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteracaoDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testAlterarChavePix_DadosInvalidos() throws Exception {
        UUID chaveId = UUID.randomUUID();
        ChavePixAlteracaoDTO alteracaoDTO = new ChavePixAlteracaoDTO();
        // Dados inválidos propositalmente:
        alteracaoDTO.setNumeroAgencia(0);
        alteracaoDTO.setNumeroConta(0);

        mockMvc.perform(put("/api/pix/" + chaveId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteracaoDTO)))
                .andExpect(status().isBadRequest()); // 400 por validação do DTO
    }


    // Testes para inativação de chave PIX
// Erro: Método não void usando doNothing()
    @Test
    void testInativarChavePix_Sucesso() throws Exception {
        UUID chaveId = UUID.randomUUID();
        ChavePixResponseDTO responseMock = new ChavePixResponseDTO();

        when(chavePixService.inativar(chaveId)).thenReturn(responseMock);

        mockMvc.perform(delete("/api/pix/" + chaveId))
                .andExpect(status().isOk());
    }

    @Test
    void testInativarChavePix_JaInativa() throws Exception {
        UUID chaveId = UUID.randomUUID();

        doThrow(new ChavePixException("Chave já inativa"))
                .when(chavePixService).inativar(chaveId);

        mockMvc.perform(delete("/api/pix/" + chaveId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorMessage").value("Chave já inativa"));
    }

    // Testes para consultas
    @Test
    void testConsultarPorTipoDeChave() throws Exception {
        when(chavePixService.consultarPorTipoChave("email"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pix/tipo?tipo=email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testConsultarPorAgenciaEConta() throws Exception {
        when(chavePixService.consultarPorAgenciaEConta(1234, 567890))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pix/agencia-conta?agencia=1234&conta=567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testConsultarPorId_Inexistente() throws Exception {
        UUID chaveId = UUID.randomUUID();

        when(chavePixService.consultarPorId(chaveId))
                .thenThrow(new ChavePixException("Chave PIX não encontrada para o ID informado."));

        mockMvc.perform(get("/api/pix/" + chaveId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Chave PIX não encontrada para o ID informado."));
    }

//    @Test
//    void testIncluirChavePix_CamposObrigatorios() throws Exception {
//        ChavePixRequestDTO requestDTO = new ChavePixRequestDTO(); // Campos vazios
//
//        mockMvc.perform(post("/api/pix")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isUnprocessableEntity()) // Status 422
//                .andExpect(jsonPath("$.errorMessage").value("Erro de validação"))
//                .andExpect(jsonPath("$.errors").isMap())
//                .andExpect(jsonPath("$.errors.size()").value(6)); // 6 campos inválidos
//    }

}