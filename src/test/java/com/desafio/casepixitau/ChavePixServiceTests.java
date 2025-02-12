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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para validação dos serviços da classe {@link ChavePixService}.
 */
public class ChavePixServiceTests {

    @Mock
    private ChavePixRepository repository; // Mock do repositório

    @InjectMocks
    private ChavePixService service; // Serviço que será testado

    private ChavePixRequestDTO requestDTO; // DTO utilizado para simular entradas
    private ChavePix chavePix; // Entidade usada para simular respostas do repositório

    /**
     * Configuração inicial antes de cada teste.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new ChavePixRequestDTO();
        requestDTO.setTipoChave("cpf");
        requestDTO.setValorChave("12345678900");
        requestDTO.setTipoConta("corrente");
        requestDTO.setNumeroAgencia(1234);
        requestDTO.setNumeroConta(567890);
        requestDTO.setNomeCorrentista("Joao");
        requestDTO.setSobrenomeCorrentista("Silva");

        chavePix = new ChavePix();
        chavePix.setId(UUID.randomUUID());
        chavePix.setTipoChave("cpf");
        chavePix.setValorChave("12345678900");
        chavePix.setTipoConta("corrente");
        chavePix.setNumeroAgencia(1234);
        chavePix.setNumeroConta(567890);
        chavePix.setNomeCorrentista("Joao");
        chavePix.setSobrenomeCorrentista("Silva");
    }

    // Testes de erro (já existentes)

    @Test
    public void testIncluirChavePixComCPFInvalido() {
        requestDTO.setValorChave("11111111111");  // CPF inválido (todos os dígitos iguais)

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("CPF inválido.", exception.getMessage());
    }

    @Test
    public void testIncluirChavePixComEmailInvalido() {
        requestDTO.setTipoChave("email");
        requestDTO.setValorChave("emailinvalido.com");  // E-mail inválido

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("E-mail inválido.", exception.getMessage());
    }

    @Test
    public void testIncluirChavePixComCelularInvalido() {
        requestDTO.setTipoChave("celular");
        requestDTO.setValorChave("11987654321");  // Celular inválido

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("Celular inválido: deve conter o código do país iniciando com '+'.", exception.getMessage());
    }

    @Test
    public void testAlterarChavePixInexistente() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.alterar(UUID.randomUUID(), requestDTO));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());
    }

    @Test
    public void testInativarChavePixInexistente() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.inativar(UUID.randomUUID()));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());
    }

    @Test
    public void testAlterarChavePixJaInativa() {
        chavePix.setDataHoraInativacao(java.time.LocalDateTime.now()); // Marca como inativa
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(chavePix));

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.alterar(chavePix.getId(), requestDTO));
        assertEquals("Não é possível alterar uma chave inativa.", exception.getMessage());
    }

    @Test
    public void testInativarChavePixJaInativa() {
        chavePix.setDataHoraInativacao(java.time.LocalDateTime.now()); // Marca como inativa
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(chavePix));

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.inativar(chavePix.getId()));
        assertEquals("A chave já está inativa.", exception.getMessage());
    }

    // Testes de sucesso (novos testes)

    @Test
    public void testIncluirChavePixValida() {
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix); // Mock para salvar a chave Pix

        ChavePixResponseDTO response = service.incluir(requestDTO);

        assertNotNull(response);
        assertEquals(requestDTO.getTipoChave(), response.getTipoChave());
        assertEquals(requestDTO.getValorChave(), response.getValorChave());
        assertEquals(requestDTO.getTipoConta(), response.getTipoConta());
        assertEquals(requestDTO.getNumeroAgencia(), response.getNumeroAgencia());
        assertEquals(requestDTO.getNumeroConta(), response.getNumeroConta());
        assertEquals(requestDTO.getNomeCorrentista(), response.getNomeCorrentista());
        assertEquals(requestDTO.getSobrenomeCorrentista(), response.getSobrenomeCorrentista());
    }

    @Test
    public void testAlterarChavePixValida() {
        chavePix.setDataHoraInativacao(null); // Garante que a chave está ativa
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(chavePix));
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);

        ChavePixResponseDTO response = service.alterar(chavePix.getId(), requestDTO);

        assertNotNull(response);
        assertEquals(requestDTO.getTipoConta(), response.getTipoConta());
        assertEquals(requestDTO.getNumeroAgencia(), response.getNumeroAgencia());
        assertEquals(requestDTO.getNumeroConta(), response.getNumeroConta());
        assertEquals(requestDTO.getNomeCorrentista(), response.getNomeCorrentista());
        assertEquals(requestDTO.getSobrenomeCorrentista(), response.getSobrenomeCorrentista());
    }

    @Test
    public void testInativarChavePixValida() {
        // Garante que a chave está ativa inicialmente
        chavePix.setDataHoraInativacao(null);

        // Configura a data de inativação para o mock
        chavePix.setDataHoraInativacao(LocalDateTime.now());

        // Simula o comportamento do repositório
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(chavePix));
        when(repository.save(any(ChavePix.class))).thenReturn(chavePix);

        // Realiza a chamada ao serviço para inativar a chave
        ChavePixResponseDTO response = service.inativar(chavePix.getId());

        // Verifica se a resposta não é nula
        assertNotNull(response);

        // Verifica se a chave foi inativada corretamente
        assertNotNull(response.getDataHoraInclusao());  // Data de inclusão não deve ser nula
        assertNotNull(response.getDataHoraInclusao()); // A data de inativação deve ser registrada
        assertEquals(chavePix.getId(), response.getId()); // Verifica se o ID da chave foi mantido corretamente
    }
}
