package com.desafio.casepixitau;

import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.ChavePixRepository;
import com.desafio.casepixitau.service.ChavePixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para validação negativa dos serviços da classe {@link ChavePixService}.
 *
 * Esses testes cobrem cenários em que a inclusão, alteração ou inativação de chaves Pix
 * deve falhar devido a entradas inválidas ou operações proibidas.
 */
public class ChavePixServiceTests {

    @Mock
    private ChavePixRepository repository; // Mock do repositório para isolar a camada de serviço

    @InjectMocks
    private ChavePixService service; // Serviço que será testado com o repositório mockado

    private ChavePixRequestDTO requestDTO; // DTO utilizado para simular entradas
    private ChavePix chavePix; // Entidade usada para simular respostas do repositório

    /**
     * Configuração inicial antes de cada teste.
     * Inicializa os mocks e define um objeto padrão para uso nos testes.
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

    /**
     * Testa a inclusão de uma chave Pix com CPF inválido.
     * O teste espera uma exceção {@link ChavePixException} com a mensagem "CPF inválido.".
     */
    @Test
    public void testIncluirChavePixComCPFInvalido() {
        requestDTO.setValorChave("11111111111");  // CPF com todos os dígitos iguais (inválido)

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("CPF inválido.", exception.getMessage());
    }

    /**
     * Testa a inclusão de uma chave Pix com e-mail inválido.
     * Espera-se que o serviço lance uma exceção com a mensagem "E-mail inválido.".
     */
    @Test
    public void testIncluirChavePixComEmailInvalido() {
        requestDTO.setTipoChave("email");
        requestDTO.setValorChave("emailinvalido.com");  // E-mail sem o caractere "@"

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("E-mail inválido.", exception.getMessage());
    }

    /**
     * Testa a inclusão de uma chave Pix com número de celular inválido.
     * O serviço deve rejeitar números sem o código do país.
     */
    @Test
    public void testIncluirChavePixComCelularInvalido() {
        requestDTO.setTipoChave("celular");
        requestDTO.setValorChave("11987654321");  // Celular sem o código do país

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.incluir(requestDTO));
        assertEquals("Celular inválido: deve conter o código do país iniciando com '+'.", exception.getMessage());
    }

    /**
     * Testa a tentativa de alterar uma chave Pix inexistente.
     * Deve lançar uma exceção indicando que a chave não foi encontrada.
     */
    @Test
    public void testAlterarChavePixInexistente() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.alterar(UUID.randomUUID(), requestDTO));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());
    }

    /**
     * Testa a tentativa de inativar uma chave Pix inexistente.
     * Deve lançar uma exceção indicando que a chave não foi encontrada.
     */
    @Test
    public void testInativarChavePixInexistente() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.inativar(UUID.randomUUID()));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());
    }

    /**
     * Testa a tentativa de alterar uma chave Pix que já está inativa.
     * Deve lançar uma exceção informando que não é possível alterar uma chave inativa.
     */
    @Test
    public void testAlterarChavePixJaInativa() {
        chavePix.setDataHoraInativacao(java.time.LocalDateTime.now()); // Marca a chave como inativa
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(chavePix));

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.alterar(chavePix.getId(), requestDTO));
        assertEquals("Não é possível alterar uma chave inativa.", exception.getMessage());
    }

    /**
     * Testa a tentativa de inativar uma chave Pix que já está inativa.
     * Deve lançar uma exceção indicando que a chave já foi inativada anteriormente.
     */
    @Test
    public void testInativarChavePixJaInativa() {
        chavePix.setDataHoraInativacao(java.time.LocalDateTime.now()); // Marca a chave como inativa
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(chavePix));

        ChavePixException exception = assertThrows(ChavePixException.class, () -> service.inativar(chavePix.getId()));
        assertEquals("A chave já está inativa.", exception.getMessage());
    }
}
