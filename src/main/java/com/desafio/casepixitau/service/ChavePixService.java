package com.desafio.casepixitau.service;

import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.dto.ChavePixResponseDTO;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.ChavePixRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing Pix keys (Chave Pix).
 * Provides methods for creating, retrieving, updating, and deactivating Pix keys.
 */
@Service
public class ChavePixService {

    private final ChavePixRepository repository;

    /**
     * Constructor to inject the repository dependency.
     *
     * @param repository the repository for managing Chave Pix entities.
     */
    public ChavePixService(ChavePixRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates and saves a new Pix key.
     *
     * @param dto the request DTO containing the key details.
     * @return a response DTO with the saved key details.
     */
    public ChavePixResponseDTO incluir(ChavePixRequestDTO dto) {
        validarChaveUnica(dto.getValorChave()); // Validates if the key is unique.
        validarLimiteDeChaves(dto); // Validates if the account has reached its key limit.

        ChavePix chavePix = new ChavePix();
        chavePix.setTipoChave(dto.getTipoChave());
        chavePix.setValorChave(dto.getValorChave());
        chavePix.setTipoConta(dto.getTipoConta());
        chavePix.setNumeroAgencia(dto.getNumeroAgencia());
        chavePix.setNumeroConta(dto.getNumeroConta());
        chavePix.setNomeCorrentista(dto.getNomeCorrentista());
        chavePix.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());
        chavePix.setDataHoraInclusao(LocalDateTime.now()); // Sets the creation timestamp.

        repository.save(chavePix); // Persists the Pix key.

        return toResponseDTO(chavePix);
    }

    /**
     * Retrieves a Pix key by its ID.
     *
     * @param id the unique identifier of the Pix key.
     * @return a response DTO with the key details.
     */
    public ChavePixResponseDTO consultarPorId(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada.")); // Throws exception if not found.
        return toResponseDTO(chave);
    }

    /**
     * Retrieves all Pix keys of a specific type.
     *
     * @param tipoChave the type of the Pix key (e.g., CPF, email).
     * @return a list of response DTOs with the matching keys' details.
     */
    public List<ChavePixResponseDTO> consultarPorTipoChave(String tipoChave) {
        List<ChavePix> chaves = repository.findByTipoChave(tipoChave);
        return chaves.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all Pix keys associated with a specific agency and account number.
     *
     * @param agencia the agency number.
     * @param conta   the account number.
     * @return a list of response DTOs with the matching keys' details.
     */
    public List<ChavePixResponseDTO> consultarPorAgenciaEConta(int agencia, int conta) {
        List<ChavePix> chaves = repository.findByNumeroAgenciaAndNumeroConta(agencia, conta);
        return chaves.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all Pix keys created or deactivated within a specific date range.
     *
     * @param dataInclusao   inclusion date for filtering (optional).
     * @param dataInativacao deactivation date for filtering (optional).
     * @return a list of response DTOs with the matching keys' details.
     */
    public List<ChavePixResponseDTO> consultarPorData(LocalDate dataInclusao, LocalDate dataInativacao) {
        List<ChavePix> chaves;

        if (dataInclusao != null) {
            chaves = repository.findByDataHoraInclusaoBetween(
                    dataInclusao.atStartOfDay(),
                    dataInclusao.plusDays(1).atStartOfDay()
            );
        } else if (dataInativacao != null) {
            chaves = repository.findByDataHoraInativacaoBetween(
                    dataInativacao.atStartOfDay(),
                    dataInativacao.plusDays(1).atStartOfDay()
            );
        } else {
            throw new ChavePixException("Data de inclusão ou inativação deve ser informada.");
        }

        return chaves.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing active Pix key's details.
     *
     * @param id  the unique identifier of the Pix key to update.
     * @param dto the request DTO containing updated details.
     * @return a response DTO with updated key details.
     */
    public ChavePixResponseDTO alterar(UUID id, ChavePixRequestDTO dto) {
        ChavePix chaveExistente = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        if (chaveExistente.getDataHoraInativacao() != null) { // Checks if the key is inactive.
            throw new ChavePixException("Não é possível alterar uma chave inativa.");
        }

        chaveExistente.setTipoConta(dto.getTipoConta());
        chaveExistente.setNumeroAgencia(dto.getNumeroAgencia());
        chaveExistente.setNumeroConta(dto.getNumeroConta());
        chaveExistente.setNomeCorrentista(dto.getNomeCorrentista());
        chaveExistente.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());

        repository.save(chaveExistente);

        return toResponseDTO(chaveExistente);
    }

    /**
     * Deactivates an existing active Pix key by setting its deactivation timestamp.
     *
     * @param id the unique identifier of the Pix key to deactivate.
     * @return a response DTO with updated key details.
     */
    public ChavePixResponseDTO inativar(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        if (chave.getDataHoraInativacao() != null) { // Checks if already inactive.
            throw new ChavePixException("A chave já está inativa.");
        }

        chave.setDataHoraInativacao(LocalDateTime.now()); // Sets deactivation timestamp.
        repository.save(chave);

        return toResponseDTO(chave);
    }

    /**
     * Validates that a given Pix key value is unique in the system.
     *
     * @param valorChave the value of the Pix key to validate.
     */
    private void validarChaveUnica(String valorChave) {
        Optional<ChavePix> existente = repository.findByValorChave(valorChave);
        if (existente.isPresent()) {
            throw new ChavePixException("O valor da chave já está cadastrado.");
        }
    }

    /**
     * Validates that an account has not exceeded its allowed number of Pix keys based on account type.
     *
     * @param dto the request DTO containing account details for validation.
     */
    private void validarLimiteDeChaves(ChavePixRequestDTO dto) {
        long quantidadeDeChaves = repository.countByNumeroAgenciaAndNumeroConta(
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
     * Converts a Chave Pix entity into a response DTO object for external usage.
     *
     * @param chave the entity to convert.
     * @return a response DTO with corresponding details from the entity.
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

        // Note: DataHoraInativacao is intentionally excluded from conversion
        return dto;
    }
}
