package com.desafio.casepixitau;

import com.desafio.casepixitau.dto.*;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.ChavePixRepository;
import com.desafio.casepixitau.service.ChavePixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChavePixServiceTests {

    @Mock
    private ChavePixRepository repository;

    @InjectMocks
    private ChavePixService service;

    private ChavePixRequestDTO requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new ChavePixRequestDTO();
        requestValido.setTipoChave("cpf");
        requestValido.setValorChave("12345678909");
        requestValido.setTipoConta("corrente");
        requestValido.setNumeroAgencia(1234);
        requestValido.setNumeroConta(123456);
        requestValido.setNomeCorrentista("Fulano");
    }

    @Test
    void incluir_DeveRetornarChaveSalva_QuandoDadosValidos() {
        when(repository.findByValorChave(any())).thenReturn(Optional.empty());
        when(repository.countByNumeroAgenciaAndNumeroContaAndDataHoraInativacaoIsNull(anyInt(), anyInt())).thenReturn(0L);
        when(repository.save(any())).thenReturn(new ChavePix());

        assertDoesNotThrow(() -> service.incluir(requestValido));
        verify(repository, times(1)).save(any());
    }

    @Test
    void incluir_DeveLancarExcecao_QuandoChaveDuplicada() {
        when(repository.findByValorChave(any())).thenReturn(Optional.of(new ChavePix()));

        assertThrows(ChavePixException.class, () -> service.incluir(requestValido));
    }

    @Test
    void incluir_DeveLancarExcecao_QuandoLimiteChavesAtingido() {
        when(repository.countByNumeroAgenciaAndNumeroContaAndDataHoraInativacaoIsNull(anyInt(), anyInt())).thenReturn(5L);

        assertThrows(ChavePixException.class, () -> service.incluir(requestValido));
    }

    @Test
    void consultarPorId_DeveRetornarChave_QuandoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.of(new ChavePix()));

        assertDoesNotThrow(() -> service.consultarPorId(id));
    }

    @Test
    void consultarPorId_DeveLancarExcecao_QuandoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ChavePixException.class, () -> service.consultarPorId(id));
    }

    @Test
    void alterar_DeveAtualizarChave_QuandoDadosValidos() {
        UUID id = UUID.randomUUID();
        ChavePixAlteracaoDTO dto = new ChavePixAlteracaoDTO();
        dto.setNomeCorrentista("Novo Nome");

        ChavePix chaveExistente = new ChavePix();
        when(repository.findById(any())).thenReturn(Optional.of(chaveExistente));
        when(repository.save(any())).thenReturn(chaveExistente);

        assertDoesNotThrow(() -> service.alterar(id, dto));
    }

    @Test
    void alterar_DeveLancarExcecao_QuandoChaveInativa() {
        UUID id = UUID.randomUUID();
        ChavePix chaveInativa = new ChavePix();
        chaveInativa.setDataHoraInativacao(LocalDateTime.now());
        when(repository.findById(any())).thenReturn(Optional.of(chaveInativa));

        assertThrows(ChavePixException.class, () -> service.alterar(id, new ChavePixAlteracaoDTO()));
    }

//    @Test
//    void inativar_DeveMarcarDataInativacao_QuandoChaveAtiva() {
//        UUID id = UUID.randomUUID();
//        ChavePix chaveAtiva = new ChavePix();
//        when(repository.findById(any())).thenReturn(Optional.of(chaveAtiva));
//
//        assertDoesNotThrow(() -> service.inativar(id));
//    }

    @Test
    void inativar_DeveLancarExcecao_QuandoChaveJaInativa() {
        UUID id = UUID.randomUUID();
        ChavePix chaveInativa = new ChavePix();
        chaveInativa.setDataHoraInativacao(LocalDateTime.now());
        when(repository.findById(any())).thenReturn(Optional.of(chaveInativa));

        assertThrows(ChavePixException.class, () -> service.inativar(id));
    }

    @Test
    void validarFormatoChave_DeveLancarExcecao_QuandoCpfInvalido() {
        requestValido.setValorChave("11111111111");
        when(repository.findByValorChave(any())).thenReturn(Optional.empty());

        assertThrows(ChavePixException.class, () -> service.incluir(requestValido));
    }

    @Test
    void validarFormatoChave_DeveLancarExcecao_QuandoEmailInvalido() {
        requestValido.setTipoChave("email");
        requestValido.setValorChave("emailinvalido");
        when(repository.findByValorChave(any())).thenReturn(Optional.empty());

        assertThrows(ChavePixException.class, () -> service.incluir(requestValido));
    }
}
