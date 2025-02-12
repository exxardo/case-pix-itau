package com.desafio.casepixitau.service;

import com.desafio.casepixitau.dto.ChavePixAlteracaoDTO;
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
        validarFormatoChave(dto);  // Valida o formato da chave conforme o tipo.

        ChavePix chavePix = new ChavePix();
        chavePix.setTipoChave(dto.getTipoChave());
        chavePix.setValorChave(dto.getValorChave());
        chavePix.setTipoConta(dto.getTipoConta());
        chavePix.setNumeroAgencia(dto.getNumeroAgencia());
        chavePix.setNumeroConta(dto.getNumeroConta());
        chavePix.setNomeCorrentista(dto.getNomeCorrentista());
        chavePix.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());
        chavePix.setDataHoraInclusao(LocalDateTime.now()); // Define a data/hora de inclusão.

        ChavePix savedChavePix = repository.save(chavePix); // Persiste a nova chave Pix.

        return toResponseDTO(savedChavePix);
    }

    /**
     * Consulta uma chave Pix pelo seu ID.
     *
     * @param id o identificador único da chave Pix.
     * @return um DTO de resposta com os detalhes da chave encontrada.
     */
    public ChavePixResponseDTO consultarPorId(UUID id) {
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave PIX não encontrada para o ID informado."));
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

        if (dataInclusao != null && dataInativacao != null) {
            throw new ChavePixException("Não é permitido informar ambas as datas.");
        } else if (dataInclusao != null) {
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
    public ChavePixResponseDTO alterar(UUID id, ChavePixAlteracaoDTO dto) {
        // Lógica para alterar os dados da chave PIX
        ChavePix chaveExistente = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada com o ID: " + id));

        // Verifique se a chave está ativa antes de permitir alteração
        if (chaveExistente.getDataHoraInativacao() != null) {
            throw new ChavePixException("Chave Pix com ID " + id + " está inativa e não pode ser alterada.");
        }

        // Atualiza os campos que podem ser alterados
        chaveExistente.setTipoConta(dto.getTipoConta());
        chaveExistente.setNumeroAgencia(dto.getNumeroAgencia());
        chaveExistente.setNumeroConta(dto.getNumeroConta());
        chaveExistente.setNomeCorrentista(dto.getNomeCorrentista());
        chaveExistente.setSobrenomeCorrentista(dto.getSobrenomeCorrentista());

        // Salva a chave atualizada
        ChavePix chaveAtualizada = repository.save(chaveExistente);
        return toResponseDTO(chaveAtualizada);
    }


    private void validarTipoConta(String tipoConta) {
        if (tipoConta == null || tipoConta.isEmpty()) {
            throw new ChavePixException("Tipo da conta é obrigatório.");
        }
        if (!(tipoConta.equals("corrente") || tipoConta.equals("poupanca"))) {
            throw new ChavePixException("Tipo da conta deve ser 'corrente' ou 'poupanca'.");
        }
        if (tipoConta.length() > 10) {
            throw new ChavePixException("Tipo da conta não pode ter mais de 10 caracteres.");
        }
    }

    private void validarNumeroAgencia(int numeroAgencia) {
        if (numeroAgencia <= 0 || numeroAgencia > 9999) {
            throw new ChavePixException("Número da agência deve ser um valor numérico positivo e no máximo 4 dígitos.");
        }
    }

    private void validarNumeroConta(int numeroConta) {
        if (numeroConta <= 0 || numeroConta > 99999999) {
            throw new ChavePixException("Número da conta deve ser um valor numérico positivo e no máximo 8 dígitos.");
        }
    }

    private void validarNomeCorrentista(String nomeCorrentista) {
        if (nomeCorrentista == null || nomeCorrentista.isEmpty()) {
            throw new ChavePixException("Nome do correntista é obrigatório.");
        }
        if (nomeCorrentista.length() > 30) {
            throw new ChavePixException("Nome do correntista não pode ter mais de 30 caracteres.");
        }
    }

    private void validarSobrenomeCorrentista(String sobrenomeCorrentista) {
        if (sobrenomeCorrentista != null && sobrenomeCorrentista.length() > 45) {
            throw new ChavePixException("Sobrenome do correntista não pode ter mais de 45 caracteres.");
        }
    }


    /**
     * Inativa uma chave Pix ativa existente.
     *
     * @param id o identificador único da chave a ser inativada.
     * @return um DTO de resposta com os dados atualizados da chave inativada.
     */
    public ChavePixResponseDTO inativar(UUID id) {
        // Buscar a chave no repositório
        ChavePix chave = repository.findById(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));

        // Verificar se a chave já está inativada
        if (chave.getDataHoraInativacao() != null) {
            throw new ChavePixException("A chave já foi desativada.");
        }

        // Registrar a data e hora da solicitação de desativação
        chave.setDataHoraInativacao(LocalDateTime.now());

        // Salvar a chave inativada
        ChavePix chaveInativada = repository.save(chave);

        // Retornar resposta com a data de inativação também no payload
        return toResponseDTO(chaveInativada);
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
     * Valida que uma conta não excedeu o limite permitido de chaves Pix.
     *
     * @param dto o DTO contendo os dados da conta para validação.
     */
    private void validarLimiteDeChaves(ChavePixRequestDTO dto) {
        long quantidadeDeChavesAtivas = repository.countByNumeroAgenciaAndNumeroContaAndDataHoraInativacaoIsNull(
                dto.getNumeroAgencia(),
                dto.getNumeroConta()
        );

        if (quantidadeDeChavesAtivas >= 5) {
            throw new ChavePixException("Limite de chaves atingido para esta conta.");
        }
    }

    /**
     * Valida o formato da chave conforme o tipo (CPF, e-mail, celular).
     *
     * @param dto o DTO contendo os dados da chave Pix a ser validada.
     */
    private void validarFormatoChave(ChavePixRequestDTO dto) {
        String tipoChave = dto.getTipoChave().toLowerCase();
        String valorChave = dto.getValorChave();

        switch (tipoChave) {
            case "cpf":
                if (!valorChave.matches("\\d{11}") || !validarCPF(valorChave)) {
                    throw new ChavePixException("CPF inválido.");
                }
                break;
            case "email":
                if (!valorChave.contains("@") || valorChave.length() > 77) {
                    throw new ChavePixException("E-mail inválido.");
                }
                break;
            case "celular":
                if (!valorChave.matches("\\+\\d{1,2}\\d{2,3}\\d{9}")) {
                    throw new ChavePixException("Celular inválido: deve conter o código do país iniciando com '+'.");
                }
                break;
            default:
                throw new ChavePixException("Tipo de chave inválido.");
        }
    }

    /**
     * Validação simplificada de CPF.
     *
     * @param cpf o número do CPF a ser validado.
     * @return true se o CPF for válido, false caso contrário.
     */
    private boolean validarCPF(String cpf) {
        return !cpf.chars().allMatch(ch -> ch == cpf.charAt(0));
    }

    /**
     * Converte uma entidade Chave Pix em um objeto DTO.
     *
     * @param chave a entidade a ser convertida.
     * @return um DTO contendo os detalhes da entidade fornecida.
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
        dto.setDataHoraInativacao(chave.getDataHoraInativacao());

        return dto;
    }

    public List<ChavePixResponseDTO> consultarPorNomeCorrentista(String nomeCorrentista) {
        List<ChavePix> chaves = repository.findByNomeCorrentistaContainingIgnoreCase(nomeCorrentista);

        return chaves.stream()
                .map(this::toResponseDTO) // Convertendo as chaves para o formato de resposta
                .collect(Collectors.toList());
    }
}
