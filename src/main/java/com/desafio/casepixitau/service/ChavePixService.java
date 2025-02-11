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
 * Classe de serviço responsável pelo gerenciamento de chaves Pix.
 * Fornece métodos para criar, consultar, atualizar e inativar chaves Pix.
 */
@Service
public class ChavePixService {

    private final ChavePixRepository repository;

    /**
     * Construtor para injeção de dependência do repositório.
     *
     * @param repository o repositório para gerenciar as entidades de Chave Pix.
     */
    public ChavePixService(ChavePixRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria e salva uma nova chave Pix.
     *
     * @param dto o DTO contendo os detalhes da chave Pix a ser criada.
     * @return um DTO de resposta com os detalhes da chave salva.
     */
    public ChavePixResponseDTO incluir(ChavePixRequestDTO dto) {
        validarChaveUnica(dto.getValorChave()); // Valida se a chave é única.
        validarLimiteDeChaves(dto); // Valida se o limite de chaves foi atingido.

        ChavePix chavePix = new ChavePix();
        chavePix.setTipoChave(dto.getTipoChave());
        chavePix.setValorChave(dto.getValorChave());
        chavePix.setTipoConta(dto.getTipoConta());
        chavePix.setNumeroAgencia(dto.getNumeroAgencia());
        chavePix.setNumeroConta(dto.getNumeroConta());
        chavePix.setNomeCorrentista(dto.getNomeCorrentista());
        chavePix.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());
        chavePix.setDataHoraInclusao(LocalDateTime.now()); // Define a data/hora de inclusão.

        repository.save(chavePix); // Persiste a nova chave Pix.

        return toResponseDTO(chavePix);
    }

    /**
     * Consulta uma chave Pix pelo seu ID.
     *
     * @param id o identificador único da chave Pix.
     * @return um DTO de resposta com os detalhes da chave encontrada.
     */
    public ChavePixResponseDTO consultarPorId(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada.")); // Lança exceção caso não encontre.
        return toResponseDTO(chave);
    }

    /**
     * Consulta todas as chaves Pix de um tipo específico.
     *
     * @param tipoChave o tipo da chave Pix (ex.: CPF, e-mail).
     * @return uma lista de DTOs de resposta com os detalhes das chaves encontradas.
     */
    public List<ChavePixResponseDTO> consultarPorTipoChave(String tipoChave) {
        List<ChavePix> chaves = repository.findByTipoChave(tipoChave);
        return chaves.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Consulta todas as chaves Pix associadas a uma agência e conta específicas.
     *
     * @param agencia o número da agência.
     * @param conta   o número da conta.
     * @return uma lista de DTOs de resposta com os detalhes das chaves encontradas.
     */
    public List<ChavePixResponseDTO> consultarPorAgenciaEConta(int agencia, int conta) {
        List<ChavePix> chaves = repository.findByNumeroAgenciaAndNumeroConta(agencia, conta);
        return chaves.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Consulta todas as chaves Pix criadas ou inativadas dentro de um intervalo de datas.
     *
     * @param dataInclusao   data de inclusão para filtro (opcional).
     * @param dataInativacao data de inativação para filtro (opcional).
     * @return uma lista de DTOs de resposta com os detalhes das chaves encontradas.
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
     * Atualiza os dados de uma chave Pix ativa existente.
     *
     * @param id  o identificador único da chave a ser atualizada.
     * @param dto o DTO contendo os novos dados da chave Pix.
     * @return um DTO de resposta com os dados atualizados da chave Pix.
     */
    public ChavePixResponseDTO alterar(UUID id, ChavePixRequestDTO dto) {
        ChavePix chaveExistente = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        if (chaveExistente.getDataHoraInativacao() != null) { // Verifica se a chave está inativa.
            throw new ChavePixException("Não é possível alterar uma chave inativa.");
        }

        // Atualiza os dados da entidade existente
        chaveExistente.setTipoConta(dto.getTipoConta());
        chaveExistente.setNumeroAgencia(dto.getNumeroAgencia());
        chaveExistente.setNumeroConta(dto.getNumeroConta());
        chaveExistente.setNomeCorrentista(dto.getNomeCorrentista());
        chaveExistente.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());

        repository.save(chaveExistente);

        return toResponseDTO(chaveExistente);
    }

    /**
     * Inativa uma chave Pix ativa existente, definindo a data/hora de inativação.
     *
     * @param id o identificador único da chave a ser inativada.
     * @return um DTO de resposta com os dados atualizados da chave inativada.
     */
    public ChavePixResponseDTO inativar(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        if (chave.getDataHoraInativacao() != null) { // Verifica se já está inativa.
            throw new ChavePixException("A chave já está inativa.");
        }

        // Define a data/hora de inativação
        chave.setDataHoraInativacao(LocalDateTime.now());
        repository.save(chave);

        return toResponseDTO(chave);
    }

    /**
     * Valida que o valor informado para a chave Pix é único no sistema.
     *
     * @param valorChave o valor da chave Pix a ser validado.
     */
    private void validarChaveUnica(String valorChave) {
        Optional<ChavePix> existente = repository.findByValorChave(valorChave);
        if (existente.isPresent()) {
            throw new ChavePixException("O valor da chave já está cadastrado.");
        }
    }

    /**
     * Valida que uma conta não excedeu o limite permitido de chaves Pix, baseado no tipo da conta.
     *
     * @param dto o DTO contendo os dados da conta para validação.
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
     * Converte uma entidade Chave Pix em um objeto DTO para uso externo.
     *
     * @param chave a entidade a ser convertida.
     * @return um DTO contendo os detalhes correspondentes à entidade fornecida.
     */
    private ChavePixResponseDTO toResponseDTO(ChavePix chave) {
        ChavePixResponseDTO dto = new ChavePixResponseDTO();

        // Mapeia os atributos relevantes
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
