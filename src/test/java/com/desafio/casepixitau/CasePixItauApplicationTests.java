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

class CasePixItauApplicationTests {

    @Mock
    private ChavePixRepository repository;

    @InjectMocks
    private ChavePixService service;

    private ChavePixRequestDTO requestDTO;
    private ChavePix chavePix;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("email");
        requestDTO.setValorChave("usuario@example.com");
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

    @Test
    void deveIncluirChavePixComSucesso() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);

        ChavePixResponseDTO response = service.incluir(requestDTO);

        assertNotNull(response);
        assertEquals(requestDTO.getValorChave(), response.getValorChave());
        verify(repository, times(1)).save(any(ChavePix.class));
    }

    @Test
    void naoDeveIncluirChaveDuplicada() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.of(chavePix));

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("O valor da chave já está cadastrado.", exception.getMessage());
    }

    @Test
    void naoDeveIncluirQuandoLimiteDeChavesAtingido() {
        when(repository.findByValorChave(requestDTO.getValorChave())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroConta(anyInt(), anyInt())).thenReturn(5L);

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("Limite de chaves atingido para esta conta.", exception.getMessage());
    }

    @Test
    void deveConsultarChavePorIdComSucesso() {
        UUID id = chavePix.getId();
        when(repository.findById(id)).thenReturn(Optional.of(chavePix));

        ChavePixResponseDTO response = service.consultarPorId(id);

        assertNotNull(response);
        assertEquals(chavePix.getValorChave(), response.getValorChave());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoChaveNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.consultarPorId(id));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());
    }

    @Test
    void deveInativarChaveComSucesso() {
        UUID id = chavePix.getId();
        when(repository.findById(id)).thenReturn(Optional.of(chavePix));

        ChavePixResponseDTO response = service.inativar(id);

        assertNotNull(response);
        assertNotNull(chavePix.getDataHoraInativacao());
        verify(repository, times(1)).save(chavePix);
    }

    @Test
    void naoDeveInativarChaveJaInativa() {
        UUID id = chavePix.getId();
        chavePix.setDataHoraInativacao(LocalDateTime.now());
        when(repository.findById(id)).thenReturn(Optional.of(chavePix));

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.inativar(id));
        assertEquals("A chave já está inativa.", exception.getMessage());
    }
}

