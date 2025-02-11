package com.desafio.casepixitau.service;

import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.dto.ChavePixResponseDTO;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.ChavePixRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChavePixService {

    private final ChavePixRepository repository;

    public ChavePixService(ChavePixRepository repository) {
        this.repository = repository;
    }

    /**
     * Cadastra uma nova chave PIX.
     */
    public ChavePixResponseDTO cadastrar(ChavePixRequestDTO dto) {
        validarChaveUnica(dto.getValorChave());
        validarLimiteDeChaves(dto);

        ChavePix chavePix = new ChavePix();
        chavePix.setTipoChave(dto.getTipoChave());
        chavePix.setValorChave(dto.getValorChave());
        chavePix.setTipoConta(dto.getTipoConta());
        chavePix.setNumeroAgencia(dto.getNumeroAgencia());
        chavePix.setNumeroConta(dto.getNumeroConta());
        chavePix.setNomeCorrentista(dto.getNomeCorrentista());
        chavePix.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());
        chavePix.setDataHoraInclusao(LocalDateTime.now());

        repository.save(chavePix);

        return toResponseDTO(chavePix);
    }

    /**
     * Consulta uma chave PIX por ID.
     */
    public ChavePixResponseDTO consultar(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));
        return toResponseDTO(chave);
    }

    /**
     * Lista todas as chaves PIX.
     */
    public List<ChavePixResponseDTO> listar() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Altera os dados de uma chave PIX existente.
     */
    public ChavePixResponseDTO alterar(UUID id, ChavePixRequestDTO dto) {
        ChavePix chaveExistente = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        if (chaveExistente.getDataHoraInativacao() != null) {
            throw new ChavePixException("Não é possível alterar uma chave inativa.");
        }

        // Atualiza os campos permitidos
        chaveExistente.setTipoConta(dto.getTipoConta());
        chaveExistente.setNumeroAgencia(dto.getNumeroAgencia());
        chaveExistente.setNumeroConta(dto.getNumeroConta());
        chaveExistente.setNomeCorrentista(dto.getNomeCorrentista());
        chaveExistente.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());

        repository.save(chaveExistente);

        return toResponseDTO(chaveExistente);
    }

    /**
     * Inativa uma chave PIX por ID.
     */
    public ChavePixResponseDTO inativar(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        if (chave.getDataHoraInativacao() != null) {
            throw new ChavePixException("A chave já está inativa.");
        }

        chave.setDataHoraInativacao(LocalDateTime.now());
        repository.save(chave);

        return toResponseDTO(chave);
    }

    /**
     * Valida se o valor da chave é único no banco.
     */
    private void validarChaveUnica(String valorChave) {
        Optional<ChavePix> existente = repository.findByValorChave(valorChave);
        if (existente.isPresent()) {
            throw new ChavePixException("O valor da chave já está cadastrado.");
        }
    }

    /**
     * Valida o limite de chaves por conta.
     */
    private void validarLimiteDeChaves(ChavePixRequestDTO dto) {
        long quantidadeDeChaves = repository.countByAgenciaAndConta(
                dto.getNumeroAgencia(),
                dto.getNumeroConta()
        );

        boolean isPessoaFisica = dto.getTipoConta().equalsIgnoreCase("corrente") ||
                dto.getTipoConta().equalsIgnoreCase("poupança");

        if ((isPessoaFisica && quantidadeDeChaves >= 5) || (!isPessoaFisica && quantidadeDeChaves >= 20)) {
            throw new ChavePixException("Limite de chaves atingido para esta conta.");
        }
    }

    /**
     * Converte uma entidade para um DTO de resposta.
     */
    private ChavePixResponseDTO toResponseDTO(ChavePix chave) {
        ChavePixResponseDTO dto = new ChavePixResponseDTO();
        dto.setId(chave.getId());
        dto.setTipoChave(chave.getTipoChave());
        dto.setValorChave(chave.getValorChave());
        dto.setTipoConta(chave.getTipoConta());
        dto.setNumeroAgencia(chave.getNumeroAgencia());
        dto.setNumeroConta(chave.getNumeroConta());
        dto.setNomeCorrentista(chave.getNomeCorrentista());
        dto.setSobrenomeCorrentista(chave.getSobrenomeCorrentista());
        dto.setDataHoraInclusao(chave.getDataHoraInclusao());

        return dto;
    }
}



